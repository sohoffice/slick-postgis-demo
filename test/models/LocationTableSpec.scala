package models

import models.jdbc.Point
import models.jdbc.PostgisProfile.api._
import org.scalatest.BeforeAndAfterEach
import org.slf4j.LoggerFactory
import tests.{BasePlaySpec, BaseSpec}

import org.scalatest.Matchers._

class LocationTableSpec extends BasePlaySpec with BeforeAndAfterEach {

  private val logger = LoggerFactory.getLogger(this.getClass)

  override protected def beforeEach(): Unit = {
    dbApi.database("default").withConnection(conn => {
      try {
        val sql =
          """
            |CREATE TABLE IF NOT EXISTS locations (
            |  id SERIAL PRIMARY KEY,
            |  name VARCHAR(40),
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
        val sql = "DELETE FROM locations"
        logger.info(s"Execute default sql: $sql")
        val prep = conn.prepareStatement(sql)
        prep.execute()
      } catch {
        case t: Throwable =>
          logger.info(s"default sql error ${t.getMessage}")
      }
    })
  }

  behavior of "LocationTable"

  it should "insert records" in {
    import models.tables.LocationTables._
    val action = locationQuery += (
      0, "", Point(0, 0), Some(Point(25, 25))
    )
    db.run(action) flatMap { _ =>
      db.run(locationQuery.map(_.model).result.map(_.headOption))
    } map {
      case Some(loc) =>
        loc.location should ===(Point(0, 0))
        loc.optionLocation shouldBe 'defined
        loc.optionLocation.get should ===(Point(25, 25))
      case _ =>
        fail("location not found")
    }
  }
  it should "filter by exact point" in {
    import models.tables.LocationTables._
    val actions = Seq(
      locationQuery += (1, "", Point(0, 0), Some(Point(25, 25))),
      locationQuery += (2, "", Point(1, 1), None)
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
        loc.optionLocation.get should ===(Point(25, 25))
      case _ =>
        fail("location not found")
    }
  }
  it should "sort by distance" in {
    import models.tables.LocationTables._
    val actions = Seq(
      locationQuery += (3, "", Point(0, 0), Some(Point(25, 25))),
      locationQuery += (4, "", Point(1, 1), None)
    )
    db.run(DBIO.sequence(actions).transactionally) flatMap { _ =>
      val q = locationQuery
        .sortBy(_.location.distance(Point(0.1, 0.1)))
        .map(_.model)

      logger.debug(s"query by distance: ${q.result.statements}")
      db.run(q.result.map(_.headOption))
    } map {
      case Some(loc) =>
        loc.location should ===(Point(0, 0))
        loc.optionLocation shouldBe 'defined
        loc.optionLocation should ===(Some(Point(25, 25)))
      case _ =>
        fail("location not found")
    }
  }

}
