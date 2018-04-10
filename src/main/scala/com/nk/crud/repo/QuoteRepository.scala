package com.nk.crud.repo

import com.nk.crud.domain.Image
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.{Document, MongoCollection, SingleObservable, result}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

class QuoteRepository(collection: MongoCollection[Image])(implicit ec: ExecutionContext) {





  def shuffleQuotes(quotes:List[String]): String = {
    val shuffledArray = Random.shuffle(quotes)
      shuffledArray(0)
  }


}
