package models.jdbc

import java.sql.Connection

import org.postgis.PGgeometry
import org.postgresql.PGConnection
import slick.jdbc.DatabaseUrlDataSource

class GeoDatabaseUrlDataSource extends DatabaseUrlDataSource {
  override def getConnection(): Connection = {
    val c = super.getConnection()
    val cls = Class.forName("org.postgis.PGgeometry").asInstanceOf[Class[PGgeometry]]
    c.asInstanceOf[PGConnection].addDataType("geography", cls)
    logger.trace(s"Added geography data type to connection: $c")
    c
  }
}
