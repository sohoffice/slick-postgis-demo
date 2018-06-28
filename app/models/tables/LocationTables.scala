package models.tables

import models.Locations.Location
import models.jdbc.Point
import models.jdbc.PostgisProfile.api._
import slick.lifted
import slick.lifted.{ProvenShape, Rep}

object LocationTables {

  type LocationRow = (Int, String, Point, Option[Point])

  class LocationTable(tag: Tag)
    extends Table[LocationRow](tag, "locations") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name")
    def location: Rep[Point] = column[Point]("location")
    def optionLocation: Rep[Option[Point]] = column[Option[Point]]("option_location")

    // Every table needs a * projection with the same type as the table's type parameter
    def * : ProvenShape[LocationRow] = (id, name, location, optionLocation)

    def model = (id, location, optionLocation) <> (Location.tupled, Location.unapply)
  }
  lazy val locationQuery: lifted.TableQuery[LocationTable] = lifted.TableQuery[LocationTable]
}
