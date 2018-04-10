package com.nk.crud

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.Materializer
import com.github.fakemongo.async.FongoAsync
import com.mongodb.async.client.MongoDatabase
import com.nk.crud.domain.Image
import com.nk.crud.repo.ImageRepository
import com.nk.crud.resource.ImageEndpoint
import com.typesafe.config.ConfigFactory
import org.mongodb.scala.MongoCollection
import org.scalatest.{BeforeAndAfterAll, FeatureSpec, Matchers}

import com.nk.crud.database.Mongo

import scala.concurrent.ExecutionContext

class ImageEndpointFeature
  extends FeatureSpec
    with Matchers
    with ScalatestRouteTest
    with BeforeAndAfterAll
    with ImageEndpoint {

  override val mat: Materializer = materializer
  override val ec: ExecutionContext = executor

  val db: MongoDatabase = {
    val fongo = new FongoAsync("akka-http-mongodb-microservice")
    val db = fongo.getDatabase(ConfigFactory.load().getString("mongo.database"))
    db.withCodecRegistry(Mongo.codecRegistry)
  }

  override val repository: ImageRepository = new ImageRepository(MongoCollection(db.getCollection("col", classOf[Image])))

  val httpEntity: (String) => HttpEntity.Strict = (str: String) => HttpEntity(ContentTypes.`application/json`, str)

  feature("image api") {
    scenario("success creation") {
      val validImage =
        """
          {
            "name": "gabfssilva",
            "location": 24
          }
        """

      Post(s"/api/images", httpEntity(validImage)) ~> Route.seal(imageRoute) ~> check {
        status shouldBe StatusCodes.Created
      }
    }

    scenario("success get after success creation") {
      val validImage =
        """
          {
            "name": "gabfssilva",
            "location": 24
          }
        """

      Post(s"/api/images", httpEntity(validImage)) ~> Route.seal(imageRoute) ~> check {
        status shouldBe StatusCodes.Created

        Get(header("Location").orNull.value()) ~> Route.seal(imageRoute) ~> check {
          status shouldBe StatusCodes.OK
        }
      }
    }


    scenario("invalid id on get") {
      Get(s"/api/images/1") ~> Route.seal(imageRoute) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

  }
}
