package controllers

import javax.inject._

import play.api.mvc._
import slick.driver.MySQLDriver.api._

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.util.Random
import play.api.libs.ws._
import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(cc: ControllerComponents, ws: WSClient) extends AbstractController(cc) {

  def index = Action {
    // 配信可能なクリエイティブ一覧を取得
    val db = Database.forURL("jdbc:mysql://localhost/onesp_master_db", driver="com.mysql.jdbc.Driver", user="root", password="root")
    val query = sql"SELECT creative_id, file_name FROM creative".as[(Long, String)]
    val f:Future[Vector[(Long,String)]] = db.run(query)
    val creatives = Await.result(f, Duration.Inf).toMap

    // 各DSPにRTBリクエスト
    val dspRes1 = Await.result(ws.url("http://localhost:9000/post/1").get(), Duration.Inf)
    val dspRes2 = Await.result(ws.url("http://localhost:9000/post/2").get(), Duration.Inf)
    val dspRes3 = Await.result(ws.url("http://localhost:9000/post/3").get(), Duration.Inf)
    val dspRes4 = Await.result(ws.url("http://localhost:9000/post/4").get(), Duration.Inf)
    val dspCreatives = Seq(dspRes1,dspRes2,dspRes3,dspRes4)

    // 各DSPの入札結果を元に内部オークション (今回はランダムオークション)
    val r = new Random()
    val i = r.nextInt(dspCreatives.length)
    Ok(views.html.adslot(dspCreatives(i).body, creatives((dspCreatives(i).json \ "image_id").as[Long])))
  }

}
