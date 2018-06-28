package models.jdbc

import java.sql.{PreparedStatement, ResultSet}

import models.jdbc.GeoJdbcTypes.Point
import org.postgis.{Geometry, PGgeometry}
import slick.SlickException
import slick.jdbc.{JdbcProfile, JdbcTypesComponent}

trait GeoJdbcTypesComponent extends JdbcTypesComponent {
  self: JdbcProfile =>

  /** Every time we read or write database of Point type, we need to use PGgeometry.
    * A Shape along won't help us, as it simply handles the query.
    *
    * We need supply a JdbcType to the driver.
    *
    * All related tables must import our new profile rather than the default PostgresProfile.api._
    */
  class PointDriverJdbcType extends DriverJdbcType[Point] {
    override def sqlType: Int = java.sql.Types.OTHER

    override def setValue(v: Point, p: PreparedStatement, idx: Int): Unit = {
      val g = new PGgeometry(new org.postgis.Point(v.lon.doubleValue(), v.lat.doubleValue()))
      p.setObject(idx, g)
    }

    override def getValue(r: ResultSet, idx: Int): Point = {
      r.getObject(idx, classOf[PGgeometry]) match {
        case g: PGgeometry if g.getGeoType == Geometry.POINT =>
          val p = g.getGeometry.asInstanceOf[org.postgis.Point]
          Point(p.x, p.y)
        case _ =>
          throw new SlickException("Database geometry data is not a point.")
      }
    }

    override def updateValue(v: Point, r: ResultSet, idx: Int): Unit = {
      val g = new PGgeometry(new org.postgis.Point(v.lon.doubleValue(), v.lat.doubleValue()))
      r.updateObject(idx, g)
    }
  }

  private val _geoPointJdbcType = new PointDriverJdbcType

  trait GeoJdbcTypes {
    val geoPointJdbcType = _geoPointJdbcType
  }

  trait GeoImplicitColumnTypes {
    implicit val geoPointJdbcType = _geoPointJdbcType
  }
}
