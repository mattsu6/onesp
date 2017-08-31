package services

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future

final case class PostData(id: PostId, title: String, body: Long)

class PostId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object PostId {
  def apply(raw: String): PostId = {
    require(raw != null)
    new PostId(Integer.parseInt(raw))
  }
}


class PostExecutionContext @Inject()(actorSystem: ActorSystem) extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the PostRepository.
  */
trait PostRepository {
  def create(data: PostData)(implicit mc: MarkerContext): Future[PostId]

  def list()(implicit mc: MarkerContext): Future[Iterable[PostData]]

  def get(id: PostId)(implicit mc: MarkerContext): Future[Option[PostData]]
}

/**
  * A trivial implementation for the Post Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class PostRepositoryImpl @Inject()()(implicit ec: PostExecutionContext) extends PostRepository {

  private val logger = Logger(this.getClass)

  private val postList = List(
    PostData(PostId("1"), "dsp1", 1),
    PostData(PostId("2"), "dsp2", 2),
    PostData(PostId("3"), "dsp3", 3),
    PostData(PostId("4"), "dsp4", 4),
    PostData(PostId("5"), "dsp5", 5)
  )

  override def list()(implicit mc: MarkerContext): Future[Iterable[PostData]] = {
    Future {
      logger.trace(s"list: ")
      postList
    }
  }

  override def get(id: PostId)(implicit mc: MarkerContext): Future[Option[PostData]] = {
    Future {
      logger.trace(s"get: id = $id")
      postList.find(post => post.id == id)
    }
  }

  def create(data: PostData)(implicit mc: MarkerContext): Future[PostId] = {
    Future {
      logger.trace(s"create: data = $data")
      data.id
    }
  }

}
