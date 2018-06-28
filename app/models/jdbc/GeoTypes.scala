package models.jdbc

import slick.ast
import slick.ast.{BaseTypedType, NumericTypedType, ScalaBaseType, ScalaType, Type}

import scala.reflect.ClassTag

trait GeoTypedType

class GeoScalaType[T](implicit val classTag: ClassTag[T]) extends ScalaType[T] with BaseTypedType[T] {
  override def nullable: Boolean = false

  override def ordered: Boolean = false

  override def scalaOrderingFor(ord: ast.Ordering) = new Ordering[T] {
    override def compare(x: T, y: T): Int = 0
  }
}

class PointType[T](val p: Point => T)(implicit tag: ClassTag[T])
  extends GeoScalaType[T]()(tag) with GeoTypedType {
}

object GeoTypes {
  implicit val pointType = new GeoScalaType[Point]()
}

case class Point(
  lon: BigDecimal,
  lat: BigDecimal
)
