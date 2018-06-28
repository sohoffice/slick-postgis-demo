package models.jdbc

object GeoJdbcTypes {

  case class Point(
    lon: BigDecimal,
    lat: BigDecimal
  )

}
