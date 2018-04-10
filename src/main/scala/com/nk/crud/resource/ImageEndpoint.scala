package com.nk.crud.resource


import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer

import scala.concurrent.Future
import de.heikoseeberger.akkahttpjackson.JacksonSupport._

import scala.concurrent.ExecutionContext
import com.nk.crud.domain.{FindByIdRequest, ImageResource}
import com.nk.crud.repo.ImageRepository
import org.mongodb.scala.result


trait ImageEndpoint {

  implicit val mat: Materializer
  implicit val ec: ExecutionContext

  val repository: ImageRepository

  val starterQuotes: List[String] = List("I wonder", "I realised that", "It occurred to me")
  val newsQuotes: List[String] = List("Meghan Markle", "the Syrian Conflict", "laissez-faire economics")
  val problemQuotes: List[String] = List("Hanging with your girlfriends", "", "laissez-faire economics")


  val imageRoute = {
    pathPrefix("api") {
      pathPrefix("image") {
        get {
          path(Segment).as(FindByIdRequest) { request =>
            complete {
              repository
                .findById(request.id)
                .map { optionalImage =>
                  optionalImage.map {
                    _.asResource
                  }
                }
                .flatMap {
                  case None => Future.successful(HttpResponse(status = StatusCodes.NotFound))
                  case Some(image) => Marshal(image).to[ResponseEntity].map { e => HttpResponse(entity = e) }
                }
            }
          }
        } ~ get {
          complete {
            repository.
              findAll()
          }
        } ~ post {
          entity(as[ImageResource]) { image =>
            complete {
              repository
                .save(image.asDomain)
                .map { id =>
                  HttpResponse(status = StatusCodes.Created, headers = List(Location(s"/api/images/$id")))
                }
            }
          }
        } ~ delete {
          path(Segment).as(FindByIdRequest) { request =>
            complete {
              repository.delete(request.id)

            }

          }
        }
      }
    }

  }

}


//  implicit def rejectionHandler = RejectionHandler.newBuilder()
//      .handle { case MissingCookieRejection(cookieName) =>
//        complete(HttpResponse(BadRequest, entity = "No cookies, no service!!!"))
//      }
//      .handle { case AuthorizationFailedRejection =>
//        complete((Forbidden, "You're out of your depth!"))
//      }
//      .handle { case ValidationRejection(msg, _) =>
//        complete((InternalServerError, "That wasn't valid! " + msg))
//      }
//      .handleAll[MethodRejection] { methodRejections =>
//      val names = methodRejections.map(_.supported.name)
//      complete((MethodNotAllowed, s"Can't do that! Supported: ${names mkString " or "}!"))
//    }
//      .handleNotFound { complete((NotFound, "Not here!")) }
//      .result()
//
//


//}

