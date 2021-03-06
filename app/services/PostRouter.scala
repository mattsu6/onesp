package services

import javax.inject.Inject

import controllers.PostController
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._

/**
  * Routes and URLs to the PostResource controller.
  */
class PostRouter @Inject()(controller: PostController) extends SimpleRouter {
  val prefix = "/post"

  def link(id: PostId): String = {
    s"${prefix}/${id.toString}"
  }

  override def routes: Routes = {
    case GET(p"/") =>
      controller.index

    case POST(p"/") =>
      controller.process

    case GET(p"/$id") =>
      controller.show(id)
  }

}
