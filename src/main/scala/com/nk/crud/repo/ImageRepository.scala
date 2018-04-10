package com.nk.crud.repo

import org.mongodb.scala._
import org.mongodb.scala.bson.{BsonString, ObjectId}
import com.nk.crud.domain.Image
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.DeleteResult

import scala.concurrent.{ExecutionContext, Future}

class ImageRepository(collection: MongoCollection[Image])(implicit ec: ExecutionContext) {


  def findById(id: String): Future[Option[Image]] =
    collection
      .find(Document("_id" -> new ObjectId(id)))
      .head
      .map(Option(_))

  def findAll(): Future[Seq[Image]] =
    collection
      .find()
      .toFuture()


  def delete(id: String): Unit =
    collection.deleteOne(equal("_id",id)).subscribe(
      (dr: DeleteResult) => println(dr.getDeletedCount),
      (e: Throwable) => println(s"Error when deleting the document $id: $e")
    )


  def save(image: Image): Future[String] =
    collection
      .insertOne(image)
      .head
      .map { _ => image._id.toHexString }

  def update(image: Image): Future[String] =
    collection
      .insertOne(image)
      .head
      .map { _ => image._id.toHexString }
}
