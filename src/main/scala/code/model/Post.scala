package code.model

/**
 * Created by IntelliJ IDEA.
 * User: igawa
 * Date: 11/11/08
 * Time: 5:01
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

class Post extends LongKeyedMapper[Post] with IdPK with CreatedUpdated {
  override def getSingleton = Post

  object title extends MappedString(this, 1024)
  object content extends MappedTextarea(this, 1048576) {
    override def textareaCols = 100
    override def textareaRows = 10
  }
  object owner extends MappedLongForeignKey(this, User)


}

object Post extends Post with LongKeyedMetaMapper[Post]
