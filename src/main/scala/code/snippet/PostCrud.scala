package code.snippet

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import Helpers._
import code.model.{Post, User}
import scala.xml.Unparsed

/**
 * Created by IntelliJ IDEA.
 * User: igawa
 * Date: 11/11/08
 * Time: 5:11
 * To change this template use File | Settings | File Templates.
 */

class PostCrud {

  private object selectedPost extends RequestVar[Box[Post]](Empty)

  def list = {
    val items = Post.findAll

    ".item *" #> items.map { ps =>
      ".link *" #> "%s(by %s)".format(ps.title, ps.owner.obj.map{
        _.shortName }.getOrElse("Anonymouse")) &
        ".link [href]" #> ("/show?p=" + ps.id).toString() &
        ".contents *" #> ps.content &
        ".edit [href]" #> ("/edit?p=" + ps.id).toString() &
        ".post_id *" #> ps.id
    }
  }

  def show = {
    val p = S.request.get.param("p")
    val num = {if (p != Empty) p.last else 1L}.toString
    val item = Post.findByKey(num.toLong)

    ".item *" #> item.map { ps =>
      ".link *" #> "%s(by %s)".format(ps.title, ps.owner.obj.map{
        _.shortName }.getOrElse("Anonymouse")) &
        ".link [href]" #> ("/show?p=" + ps.id).toString() &
        ".contents *" #> ps.content.is &
        ".edit [href]" #> ("/edit?p=" + ps.id).toString() &
        ".post_id *" #> ps.id
    }
  }

  def content = {
    val p = S.request.get.param("p")
    val num = {if (p != Empty) p.last else 1L}.toString
    val post = Post.findByKey(num.toLong)
    <pre>{Unparsed(post.map(_.content.is).openOr("Blog Not Found.").toString)}
    </pre>
  }

  def create = {
    var title = ""
    var content = "http://"

    def process() {
      User.currentUser match {
        case Full(user) =>
          Post.create.title(title).content(content).owner(user).save
          S.notice("記事を登録しました:" + content)
        case _ =>
          S.error("登録するにはログインしてください")
      }
      S.redirectTo("/post")
    }
    "name=title" #> SHtml.onSubmit(title = _) &
    "name=contents" #> SHtml.onSubmit(content = _) &
    "type=submit" #> SHtml.onSubmitUnit (process)
  }

  private def savePost(post: Post) = post.validate match {
    case Nil => post.save; S.redirectTo("/index")
    case x => S.error(x); selectedPost(Full(post))
  }

  def edit = {
    val p = S.request.get.param("p")
    val num = {if (p != Empty) p.last else 1L}.toString
    val post = Post.findByKey(num.toLong)

    post.map(_.
      toForm(Empty, savePost _) ++ <tr>
      <td><a href="/index">Cancel</a></td>
      <td><input type="submit" value="Save"/></td>
    </tr>
    ) openOr {S.error("Post not found"); S.redirectTo("/index")}

  }
}
