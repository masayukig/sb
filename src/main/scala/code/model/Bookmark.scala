package code.model

/**
 * Created by IntelliJ IDEA.
 * User: igawa
 * Date: 11/11/08
 * Time: 5:01
 * To change this template use File | Settings | File Templates.
 */
import net.liftweb.mapper._

class Bookmark extends LongKeyedMapper[Bookmark] with IdPK with CreatedUpdated {
  override def getSingleton = Bookmark

  object title extends MappedString(this, 512)
  object link extends MappedString(this, 1024)
  object owner extends MappedLongForeignKey(this, User)


}

object Bookmark extends Bookmark with LongKeyedMetaMapper[Bookmark]
