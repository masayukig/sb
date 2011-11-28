package code.snippet

import scala.xml._
import scala.xml.transform.RewriteRule
import net.liftweb.http.S

/**
 * Snippet for Facebook
 * Comments, Like button..
 */
class FBSnippet {
  def comments = {
    <div class="fb-comments" data-href={S.hostAndPath+S.uri+"?p="+S.param("p").openTheBox} data-num-posts="5" data-width="500"></div>
  }

  def like = {
    <div class="fb-like" data-send="true" data-width="450" data-show-faces="true"></div>
  }
}
