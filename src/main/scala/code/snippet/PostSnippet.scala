package code.snippet

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.mapper._
import Helpers._
import code.model.{Post, User}

/**
 * Created by IntelliJ IDEA.
 * User: igawa
 * Date: 11/11/08
 * Time: 5:11
 * To change this template use File | Settings | File Templates.
 */

class PostSnippet {

  def list = {
    val items = Post.findAll

    ".item *" #> items.map { ps =>
      ".link *" #> "%s(by %s)".format(ps.title, ps.owner.obj.map{
        _.shortName }.getOrElse("Anonymouse")) &
        ".link [href]" #> ("/show?p=" + ps.id).toString() &
        ".contents *" #> ps.contents &
        ".post_id *" #> ps.id
    }
  }

  def show = {
    println(S.request.get.param("p").last)
    val num = S.request.get.param("p").last
    val item = Post.findByKey(num.toLong)

    ".item *" #> item.map { ps =>
      ".link *" #> "%s(by %s)".format(ps.title, ps.owner.obj.map{
        _.shortName }.getOrElse("Anonymouse")) &
        ".link [href]" #> ("/show?p=" + ps.id).toString() &
        ".contents *" #> ps.contents &
        ".post_id *" #> ps.id
    }
  }

  def create = {
    var title = ""
    var contents = "http://"

    def process() {
      User.currentUser match {
        case Full(user) =>
          Post.create.title(title).contents(contents).owner(user).save
          S.notice("記事を登録しました:" + contents)
        case _ =>
          S.error("登録するにはログインしてください")
      }
      S.redirectTo("/post")
    }
    "name=title" #> SHtml.onSubmit(title = _) &
    "name=contents" #> SHtml.onSubmit(contents = _) &
    "type=submit" #> SHtml.onSubmitUnit (process)
  }
}
