package models.jdbc

import models.jdbc.PointColumnExtensionMethods.Operators
import slick.ast.Library.JdbcFunction
import slick.ast.ScalaBaseType._
import slick.ast._
import slick.lifted._

final class PointColumnExtensionMethods[P1](val c: Rep[P1]) extends AnyVal with ExtensionMethods[Point, P1] {
  import GeoTypes._
  protected[this] implicit def b1Type = implicitly[TypedType[Point]]

  def distance[R](s: Point)(implicit om: o#to[Double, R]) = {
    // we don't have a ScalaBaseType for Point, so we've to specify GeoTypes.pointType explicitly
    om.column(Operators.distance, n, LiteralNode(GeoTypes.pointType, s))
  }

}

object PointColumnExtensionMethods {
  object Operators {
    val distance = new JdbcFunction("ST_Distance")
  }

  trait PointColumnExtensionMethodConversions {
    implicit def pointColumnExtensionMethods[B1](c: Rep[Point]): PointColumnExtensionMethods[Point] = new PointColumnExtensionMethods[Point](c)
    implicit def pointOptionColumnExtensionMethods[B1](c: Rep[Option[Point]])(implicit tm: BaseTypedType[B1]): PointColumnExtensionMethods[Option[Point]] = new PointColumnExtensionMethods[Option[Point]](c)
  }
}
