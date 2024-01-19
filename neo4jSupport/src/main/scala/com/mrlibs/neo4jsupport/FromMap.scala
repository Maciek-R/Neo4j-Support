package com.mrlibs.neo4jsupport

trait FromMap[T] {
  def fromMap(map: Map[String, Any]): Either[FromValueExtractionError, T]
}

object FromMap {
  def apply[T](implicit FM: FromMap[T]): FromMap[T] = FM

  implicit def gen[T: FromValue]: FromMap[T] = (map: Map[String, Any]) => implicitly[FromValue[T]].fromValue(Some(map))
}
