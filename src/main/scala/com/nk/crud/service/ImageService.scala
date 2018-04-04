package com.nk.crud.service

import com.nk.crud.domain.{Image, ImageUpdate}

import scala.concurrent.{ExecutionContext, Future}

class ImageService(implicit val executionContext: ExecutionContext) {

  var images = Vector.empty[Image]

  def createImage(image: Image): Future[Option[String]] = Future {
    images.find(_.id == image.id) match {
      case Some(q) => None
      case None =>
        images = images :+ image
        Some(image.id)
    }
  }

  def getImage(id: String): Future[Option[Image]] = Future {
    images.find(_.id == id)
  }

  def updateImage(id: String, update: ImageUpdate): Future[Option[Image]] = {

    def updateEntity(image: Image): Image = {
      val title = update.name.getOrElse(image.name)
      val text = update.location.getOrElse(image.location)
      Image(id, title, text)
    }

    getImage(id).flatMap { maybeImage =>
      maybeImage match {
        case None => Future { None }
        case Some(image) =>
          val updatedImage = updateEntity(image)
          deleteImage(id).flatMap { _ =>
            createImage(updatedImage).map(_ => Some(updatedImage))
          }
      }
    }
  }

  def deleteImage(id: String): Future[Unit] = Future {
    images = images.filterNot(_.id == id)
  }


}
