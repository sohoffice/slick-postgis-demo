package models

import models.jdbc.GeoJdbcTypes.Point
import models.jdbc.PostgisProfile.api._
import org.scalatest.BeforeAndAfterEach
import org.slf4j.LoggerFactory
import tests.{BasePlaySpec, BaseSpec}
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.Matchers._

class LocationTableSpec extends BasePlaySpec with BeforeAndAfterEach {

  private val logger = LoggerFactory.getLogger(this.getClass)

  override protected def beforeEach(): Unit = {
    dbApi.database("default").withConnection(conn => {
      try {
        val sql =
          """
            |CREATE TABLE locations (
            |  id SERIAL PRIMARY KEY,
            |  location GEOGRAPHY NOT NULL,
            |  option_location GEOGRAPHY
            |)""".stripMargin
        logger.info(s"Execute default sql: $sql")
        val prep = conn.prepareStatement(sql)
        prep.execute()
      } catch {
        case t: Throwable =>
          logger.info(s"default sql error ${t.getMessage}")
      }
      super.beforeEach()
    })
  }

  override protected def afterEach(): Unit = {
    super.afterEach()
    dbApi.database("default").withConnection(conn => {
      try {
        val sql = "DROP TABLE locations"
        logger.info(s"Execute default sql: $sql")
        val prep = conn.prepareStatement(sql)
        prep.execute()
      } catch {
        case t: Throwable =>
          logger.info(s"default sql error ${t.getMessage}")
      }
    })
  }

  "LocationTable" should {
    "insert records" in {
      import models.tables.LocationTables._
      val action = locationQuery += (
        0, Point(0, 0), Some(Point(25, 25))
      )
      db.run(action) flatMap { _ =>
        db.run(locationQuery.map(_.model).result.map(_.headOption))
      } map {
        case Some(loc) =>
          loc.location should ===(Point(0, 0))
          loc.optionLocation shouldBe 'defined
          loc.optionLocation should ===(Point(25, 25))
        case _ =>
          fail("location not found")
      }
    }
    "filter by exact point" in {
      import models.tables.LocationTables._
      val actions = Seq(
        locationQuery += (0, Point(0, 0), Some(Point(25, 25))),
        locationQuery += (0, Point(1, 1), None)
      )
      db.run(DBIO.sequence(actions)) flatMap { _ =>
        val q = locationQuery
            .filter(_.location === Point(0, 0))
            .map(_.model)
        db.run(q.result.map(_.headOption))
      } map {
        case Some(loc) =>
          loc.location should ===(Point(0, 0))
          loc.optionLocation shouldBe 'defined
          loc.optionLocation should ===(Point(25, 25))
        case _ =>
          fail("location not found")
      }
    }
  }

}
