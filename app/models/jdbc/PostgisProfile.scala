package models.jdbc

import models.jdbc.PointColumnExtensionMethods.PointColumnExtensionMethodConversions
import slick.jdbc.PostgresProfile
import slick.jdbc.hikaricp.HikariCPJdbcDataSource

class PostgisHikariCpJdbcDataSource(val _ds: com.zaxxer.hikari.HikariDataSource, val _hconf: com.zaxxer.hikari.HikariConfig) extends HikariCPJdbcDataSource(_ds, _hconf)

trait PostgisProfile extends PostgresProfile with GeoJdbcTypesComponent {
  override val columnTypes: JdbcTypes with GeoJdbcTypes = new JdbcTypes with GeoJdbcTypes

  override val api = new API with GeoImplicitColumnTypes with PointColumnExtensionMethodConversions
}

object PostgisProfile extends PostgisProfile