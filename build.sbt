import Dependencies._

name := "slick-postgis-demo"
 
version := "1.0" 
      
lazy val `slick-postgis-demo` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( postgresql, postgis, mockito, scalaTest, guice ) ++ slick

unmanagedResourceDirectories in Test +=  baseDirectory ( _ /"target/web/public/test" ).value

      