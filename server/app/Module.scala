import com.google.inject.AbstractModule
import jp.co.applibot.abc.database.interface.UserStore
import jp.co.applibot.abc.database.memory.UserMemoryStore

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[UserStore]) to classOf[UserMemoryStore] asEagerSingleton()
  }
}
