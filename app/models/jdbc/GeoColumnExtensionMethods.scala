package models.jdbc

import models.jdbc.GeoColumnExtensionMethods.Operators
import slick.ast.Library.{JdbcFunction, SqlOperator}
import slick.ast._
import slick.lifted._
import ScalaBaseType._

trait GeoColumnExtensionMethods[B1, P1] extends Any with ExtensionMethods[B1, P1] {

  def distance[R](s: Point)(implicit om: o#to[Double, R]) =
    om.column(Operators.distance, n, LiteralNode(GeoTypes.pointType, s))

}

final class BaseGeoColumnExtensionMethods[P1](val c: Rep[P1]) extends AnyVal with GeoColumnExtensionMethods[P1, P1] with BaseExtensionMethods[P1]

final class OptionGeoColumnExtensionMethods[B1](val c: Rep[Option[B1]]) extends AnyVal with GeoColumnExtensionMethods[B1, Option[B1]] with OptionExtensionMethods[B1]

object GeoColumnExtensionMethods {
  object Operators {
    val distance = new JdbcFunction("ST_Distance")
  }

  trait GeoColumnExtensionMethodConversions {
    implicit def geoColumnExtensionMethods[B1](c: Rep[B1])(implicit tm: BaseTypedType[B1] with GeoTypedType): BaseGeoColumnExtensionMethods[B1] = new BaseGeoColumnExtensionMethods[B1](c)
    implicit def geoOptionColumnExtensionMethods[B1](c: Rep[Option[B1]])(implicit tm: BaseTypedType[B1] with GeoTypedType): OptionGeoColumnExtensionMethods[B1] = new OptionGeoColumnExtensionMethods[B1](c)
  }
}
