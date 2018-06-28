package tests

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, DoNotDiscover, TestSuite}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.{Application, Mode}
import play.api.db.DBApi
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.evolutions.EvolutionsModule
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import slick.jdbc.JdbcProfile

trait BasePlaySpec extends BaseSpec with GuiceOneAppPerSuite with BeforeAndAfterEach {

  override val fakeApplication: Application = new GuiceApplicationBuilder()
    .in(Mode.Test)
    .bindings(new EvolutionsModule)
    .build

  val injector: Injector = fakeApplication.injector
  protected val databaseConfigProvider = injector.instanceOf[DatabaseConfigProvider]

  protected lazy val dbConfig = databaseConfigProvider.get[JdbcProfile]
  protected lazy val db = dbConfig.db

  protected lazy val dbApi = injector.instanceOf[DBApi]

}
