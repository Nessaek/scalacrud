package com.nk.crud

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.nk.crud.database.Mongo
import com.nk.crud.repo.ImageRepository
import com.nk.crud.resource.ImageEndpoint

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}


object Main extends App with ImageEndpoint {


  implicit val sys: ActorSystem = ActorSystem("akka-http-mongodb-microservice")
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = sys.dispatcher

  val log = sys.log

  override val repository: ImageRepository = new ImageRepository(Mongo.imageCollection)

  Http().bindAndHandle(imageRoute, "0.0.0.0", 8080).onComplete {
    case Success(b) => log.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
    case Failure(e) => log.error(s"could not start application: {}", e.getMessage)
  }
}
