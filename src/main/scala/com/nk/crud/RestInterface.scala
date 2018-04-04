package com.nk.crud

import akka.http.scaladsl.server.Route
import com.nk.crud.resource.ImageResource
import com.nk.crud.service.ImageService

import scala.concurrent.ExecutionContext

trait RestInterface extends Resources {

  implicit def executionContext: ExecutionContext

  lazy val imageService = new ImageService

  val routes: Route = emptyRoutes

}

trait Resources extends ImageResource
