package models.jdbc

import java.sql.{PreparedStatement, ResultSet}

import org.postgis.{Geometry, PGgeometry}
import org.slf4j.LoggerFactory
import slick.SlickException
import slick.ast.{FieldSymbol, Type}
import slick.jdbc.{JdbcProfile, JdbcType, JdbcTypesComponent}

trait GeoJdbcTypesComponent extends JdbcTypesComponent {
  self: JdbcProfile =>

  /** Every time we read or write database of Point type, we need to use PGgeometry.
    * A Shape along won't help us, as it simply handles the query.
    *
    * We need supply a JdbcType to the driver.
    *
    * All related tables must import our new profile rather than the default PostgresProfile.api._
    */
  class PointDriverJdbcType extends DriverJdbcType[Point] with GeoTypedType {
    override def sqlType: Int = java.sql.Types.OTHER

    override def sqlTypeName(sym: Option[FieldSymbol]): String = "geometry"

    override def setValue(v: Point, p: PreparedStatement, idx: Int): Unit = {
      val g = new PGgeometry(new org.postgis.Point(v.lon.doubleValue(), v.lat.doubleValue()))
      p.setObject(idx, g)
    }

    override def getValue(r: ResultSet, idx: Int): Point = {
      r.getObject(idx) match {
        case g: PGgeometry if g.getGeoType == Geometry.POINT =>
          val p = g.getGeometry.asInstanceOf[org.postgis.Point]
          Point(p.x, p.y)
        case null =>
          null
        case x =>
          throw new SlickException(s"Database geometry data is not a point, it was: $x")
      }
    }

    override def updateValue(v: Point, r: ResultSet, idx: Int): Unit = {
      val g = new PGgeometry(new org.postgis.Point(v.lon.doubleValue(), v.lat.doubleValue()))
      r.updateObject(idx, g)
    }

    override def hasLiteralForm: Boolean = false
  }

  private val _geoPointJdbcType = new PointDriverJdbcType

  trait GeoJdbcTypes {
    val geoPointJdbcType = _geoPointJdbcType
  }

  trait GeoImplicitColumnTypes {
    implicit val geoPointJdbcType = _geoPointJdbcType
  }

  override def jdbcTypeFor(t: Type) = {
    ((t.structural match {
      case GeoTypes.pointType => _geoPointJdbcType
      case _ =>
        super.jdbcTypeFor(t)
    }): JdbcType[_]).asInstanceOf[JdbcType[Any]]
  }

  private val logger = LoggerFactory.getLogger(this.getClass)
}
