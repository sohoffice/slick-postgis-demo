package models.jdbc

import java.sql.{PreparedStatement, ResultSet}

import org.postgis.{Geometry, PGgeometry}
import org.slf4j.LoggerFactory
import slick.SlickException
import slick.ast.{FieldSymbol, Type}
import slick.jdbc.{JdbcProfile, JdbcType, JdbcTypesComponent}

trait GeoJdbcTypesComponent extends JdbcTypesComponent {
  self: JdbcProfile =>

  /** We'll use the PGgeometry object supplied by postgis-jdbc project to access the database.
    * It can be used by using the standard JDBC interface: getObject() and setObject().
    *
    * To use the PGgeometry object, we must provide a DriverJdbcType to slick.
    *
    * All related tables must now import our new profile rather than the default PostgresProfile.api._
    *
    * ```
    * import models.jdbc.PostgisProfile.api._
    * ```
    */
  class PointDriverJdbcType extends DriverJdbcType[Point] {
    override def sqlType: Int = java.sql.Types.OTHER

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
