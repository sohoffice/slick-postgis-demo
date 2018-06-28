package models

import models.jdbc.GeoJdbcTypes.Point
import models.jdbc.PostgisProfile.api._
import slick.lifted.Rep

object Locations {
  case class Location(
    id: Int,
    location: Point,
    optionLocation: Option[Point]
  )
  case class LiftedLocation(
    id: Rep[Int],
    location: Rep[Point],
    optionLocation: Rep[Option[Point]]
  )

  implicit object Shape extends CaseClassShape(LiftedLocation.tupled, Location.tupled)

}
