package com.mrlibs.neo4jsupport

import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import shapeless.tag.@@

class FromValueTest extends AnyFlatSpec with Matchers with EitherValues {

  it should "convert string" in {
    FromValue.apply[String].fromValue(Some("text")) shouldBe Right("text")
  }
  it should "return error when converting None to String" in {
    FromValue.apply[String].fromValue(None) shouldBe Left(MissingFieldError)
  }
  it should "convert option string" in {
    FromValue.apply[Option[String]].fromValue(Some("text")) shouldBe Right(Some("text"))
  }
  it should "convert option string to None when given None" in {
    FromValue.apply[Option[String]].fromValue(None) shouldBe Right(None)
  }
  it should "convert int" in {
    FromValue.apply[Int].fromValue(Some(123)) shouldBe Right(123)
  }
  it should "return error when converting None to Int" in {
    FromValue.apply[Int].fromValue(None) shouldBe Left(MissingFieldError)
  }
  it should "convert option int" in {
    FromValue.apply[Option[Int]].fromValue(Some(124)) shouldBe Right(Some(124))
  }
  it should "convert option Int to None when given None" in {
    FromValue.apply[Option[Int]].fromValue(None) shouldBe Right(None)
  }
  it should "convert boolean" in {
    FromValue.apply[Boolean].fromValue(Some(true)) shouldBe Right(true)
  }
  it should "return error when converting None to Boolean" in {
    FromValue.apply[Boolean].fromValue(None) shouldBe Left(MissingFieldError)
  }
  it should "convert option boolean" in {
    FromValue.apply[Option[Boolean]].fromValue(Some(false)) shouldBe Right(Some(false))
  }
  it should "convert option Boolean to None when given None" in {
    FromValue.apply[Option[Boolean]].fromValue(None) shouldBe Right(None)
  }

  it should "return error when converting int to string" in {
    FromValue.apply[String].fromValue(Some(123)) shouldBe Left(ConversionError("Cannot convert 123 into String"))
  }
  it should "return error when converting int to option string" in {
    FromValue.apply[Option[String]].fromValue(Some(123)) shouldBe Left(
      ConversionError("Cannot convert 123 into String")
    )
  }

  trait AnyTag

  it should "convert tagged string" in {
    val text: String @@ AnyTag = shapeless.tag[AnyTag][String]("text")
    FromValue.apply[String @@ AnyTag].fromValue(Some(text)) shouldBe Right("text")
  }
  it should "return error when converting None to tagged String" in {
    FromValue.apply[String @@ AnyTag].fromValue(None) shouldBe Left(MissingFieldError)
  }
  it should "convert option string tagged" in {
    val text: String @@ AnyTag = shapeless.tag[AnyTag][String]("text")
    FromValue.apply[Option[String @@ AnyTag]].fromValue(Some(text)) shouldBe Right(Some("text"))
  }
  it should "convert option string tagged to None when given None" in {
    FromValue.apply[Option[String @@ AnyTag]].fromValue(None) shouldBe Right(None)
  }
}
