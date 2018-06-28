import play.sbt.PlayImport._
import sbt._

object Dependencies {

  val slick = Seq(
    "com.typesafe.play" %% "play-slick" % "3.0.0" withSources(),
    "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0" withSources()
  )

  val scalaTest = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

  val mockito = "org.mockito" % "mockito-all" % "1.10.19" % Test

  val postgresql = "org.postgresql" % "postgresql" % "42.2.2"
  val postgis = "net.postgis" % "postgis-jdbc" % "2.2.1" withSources()

  val logback = Seq(
    "ch.qos.logback" % "logback-core" % "1.2.3" withSources(),
    "ch.qos.logback" % "logback-classic" % "1.2.3" withSources()
  )

}
