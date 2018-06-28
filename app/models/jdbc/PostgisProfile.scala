package models.jdbc

import slick.jdbc.PostgresProfile

trait PostgisProfile extends PostgresProfile with GeoJdbcTypesComponent {
  override val columnTypes: JdbcTypes with GeoJdbcTypes = new JdbcTypes with GeoJdbcTypes

  override val api = new API with GeoImplicitColumnTypes
}

object PostgisProfile extends PostgisProfile