package com.nk.crud.domain

import org.bson.types.ObjectId
import org.mongodb.scala.bson.ObjectId

case class FindByIdRequest(id: String) {
  require(ObjectId.isValid(id), "the informed id is not a representation of a valid hex string")
}

case class ImageResource(id: String, name: String, location: String){
  //Do I need this if else?
  def asDomain = Image(if(id==null) ObjectId.get else new ObjectId(id),name,location)
}

case class Image(_id: ObjectId, name: String, location: String) {
  def asResource = ImageResource(_id.toHexString, name,location)
}

