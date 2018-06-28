package tests

import org.scalatest._
import org.scalatest.words.{MatcherWords, ShouldVerb}
import org.scalatestplus.play.{PlaySpec, WsScalaTestClient}
import play.api.mvc.Results

trait BaseSpec extends TestSuite with Matchers with OptionValues with WsScalaTestClient with Results with AsyncFlatSpecLike
