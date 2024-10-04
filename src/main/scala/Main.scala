import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.RouteResult.Complete
import org.apache.pekko.http.scaladsl.model.HttpEntity
import org.apache.pekko.http.scaladsl.model.ContentType
import org.apache.pekko.http.scaladsl.model.ContentTypes
import scalatags.Text.all._
import scalatags.Text.all
import org.apache.pekko.http.scaladsl.server.Route
import scala.collection.mutable.ListBuffer
@main
def hello(): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext = system.executionContext
    val todoItemsDao = TodoItemsDao()
    val route: Route = concat(
      get {
          path("") {
              complete(
                HttpEntity(
                  ContentTypes.`text/html(UTF-8)`,
                  View.getPage(
                    ul(id := "list")("nothing..."),
                    form(
                      attr("hx-post") := "/todo-items",
                      attr("hx-target") := "#list"
                    )(
                      input(
                        `type` := "text",
                        placeholder := "Todo item content..."
                      ),
                      button("add")
                    )
                  )
                )
              )
          }
      },
      post {
          path("todo-items") {
              complete(
                HttpEntity(
                  ContentTypes.`text/html(UTF-8)`, {
                      todoItemsDao.save()
                  }
                )
              )
          }
      }
    )
    Http()
        .newServerAt("localhost", 8080)
        .bind(route)
}

case class TodoItem(id: Option[Integer], task: String, done: Boolean)

class TodoItemsDao {
    val buffer: ListBuffer[TodoItem] = ListBuffer()

    def save(item: TodoItem): TodoItem = {
        val newId: Integer = item.id.getOrElse(buffer.size)
        val itemWithId = item.copy(id = Some(newId))

        buffer.addOne(itemWithId)
        itemWithId
    }
    def findAll(): List[TodoItem] = {
        buffer.toList
    }
}

object JsonManager {
    private def fromJson[T](toInstantiate: T, json: String) = {
      val typeTag: Int
    }
}

def cast[T: scala.reflect.ClassTag](x: Any): Option[T] = x match {
    case v: T => Some(v)
    case _    => Option.empty[T]
}
object View {
    def getPage(pageContent: scalatags.Text.all.Frag*): String = {
        "<!DOCTYPE html>" + html(
          all.head(
            meta(charset := "UTF8")(),
            meta(
              name := "viewport",
              content := "width=device-width, initial-scale=1"
            )(),
            raw(
              "<script defer src='https://cdn.jsdelivr.net/npm/alpinejs@3.x.x/dist/cdn.min.js'></script>"
            ),
            raw(
              "<script src='https://unpkg.com/htmx.org@2.0.2' integrity='sha384-Y7hw+L/jvKeWIRRkqWYfPcvVxHzVzn5REgzbawhxAuQGwX1XWe70vji+VSeHOThJ' crossorigin='anonymous'></script>"
            ),
            raw(
              "<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bulma@1.0.2/css/bulma.min.css'>"
            )
          ),
          body(pageContent)
        )
    }
}
