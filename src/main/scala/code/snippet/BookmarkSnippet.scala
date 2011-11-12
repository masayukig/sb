package code.snippet

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.mapper._
import Helpers._
import code.model.{User, Bookmark}

/**
 * Created by IntelliJ IDEA.
 * User: igawa
 * Date: 11/11/08
 * Time: 5:11
 * To change this template use File | Settings | File Templates.
 */

class BookmarkSnippet {

  def list = {



    val items = Bookmark.findAll(By(

      Bookmark.owner,

      User.currentUser.get))

    ".item *" #> items.map { bm =>
      ".link *" #> "%s(by %s)".format(bm.title, bm.owner.obj.map{
      _.niceName }.getOrElse("Anonymouse")) &
      ".link [href]" #> bm.link
    }
  }

  def create = {
    var title = ""
    var link = "http://"

    def process() {
      User.currentUser match {
        case Full(user) =>
          Bookmark.create.title(title).link(link).owner(user).save
          S.notice("ブックマークを登録しました")
        case _ =>
          S.error("登録するにはログインしてください")
      }
      S.redirectTo("/bookmark")
    }
    "name=title" #> SHtml.onSubmit(title = _) &
    "name=link" #> SHtml.onSubmit(link = _) &
    "type=submit" #> SHtml.onSubmitUnit (process)
  }
}
