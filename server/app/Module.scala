import com.google.inject.AbstractModule
import jp.co.applibot.abc.database.interface.{ChatRoomStore, MessageStore, UserStore}
import jp.co.applibot.abc.database.memory.{ChatRoomMemoryStore, MessageMemoryStore, UserMemoryStore}

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UserStore]) to classOf[UserMemoryStore] asEagerSingleton()
    bind(classOf[ChatRoomStore]) to classOf[ChatRoomMemoryStore] asEagerSingleton()
    bind(classOf[MessageStore]) to classOf[MessageMemoryStore] asEagerSingleton()
  }
}
