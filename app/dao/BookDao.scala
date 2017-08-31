package dao

package dao
import javax.inject.Inject
import models.Book
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

//クラス名の横の文はDBの定義に必要なのでおまじない
class BookDAO @Inject()(protected val dbConfigProvider:DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile]{

  import driver.api._
  private val Books = TableQuery[BooksTable]
  private class BooksTable(tag: Tag) extends Table[Book](tag,"BOOK"){
    def id = column[Long]("id",O.PrimaryKey,O.AutoInc)
    def title = column[String]("title")
    def author_id = column[Long]("author_id")
    def * = (id,title,author_id)<>(Book.tupled,Book.unapply _)
  }
  def all():Future[Seq[Book]] = db.run(Books.result)
  def insert(book: Book):Future[Unit] = db.run(Books += book).map{ _ => () }
}
