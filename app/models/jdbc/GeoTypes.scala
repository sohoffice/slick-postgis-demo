package models.jdbc

import slick.ast
import slick.ast.{BaseTypedType, NumericTypedType, ScalaBaseType, ScalaType, Type}

import scala.reflect.ClassTag

/** We do not want Point to be Ordered, actually we do not know what's the right order for Points.
  *
  * As a result we extends ScalaType rather that ScalaBaseType.
  *
  * @param classTag
  * @tparam T
  */
class PointScalaType[T](implicit val classTag: ClassTag[T]) extends ScalaType[T] with BaseTypedType[T] {
  override def nullable: Boolean = true

  override def ordered: Boolean = false

  override def scalaOrderingFor(ord: ast.Ordering) = new Ordering[T] {
    override def compare(x: T, y: T): Int = 0
  }
}

object GeoTypes {
  implicit val pointType = new PointScalaType[Point]()
}

case class Point(
  lon: BigDecimal,
  lat: BigDecimal
)
