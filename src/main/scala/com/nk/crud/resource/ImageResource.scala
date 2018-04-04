package com.nk.crud.resource

import akka.http.scaladsl.server.Route

import com.nk.crud.domain.{Image, ImageUpdate}
import com.nk.crud.routing.MyResource
import com.nk.crud.service.ImageService


trait ImageResource extends MyResource {

  val imageService: ImageService

  def imageRoutes: Route = pathPrefix("image") {
    pathEnd {
      post {
        entity(as[Image]) { image =>
          completeWithLocationHeader(
            resourceId = imageService.createImage(image),
            ifDefinedStatus = 201, ifEmptyStatus = 409)
        }
      }
    } ~
      path(Segment) { id =>
        get {
          complete(imageService.getImage(id))
        } ~
          put {
            entity(as[ImageUpdate]) { update =>
              complete(imageService.updateImage(id, update))
            }
          } ~
          delete {
            complete(imageService.deleteImage(id))
          }
      }

  }

  def emptyRoutes: Route = pathPrefix("/test"){
    get {
    // XML is marshalled to `text/xml` by default, so we simply override here
        complete("test")
        }

    }

}