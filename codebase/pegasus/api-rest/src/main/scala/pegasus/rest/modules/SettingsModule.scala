package pegasus.rest.modules

import pegasus.rest.AppConfig
import com.typesafe.config.{ Config, ConfigFactory }

trait SettingsModule {
  implicit val config = new AppConfig {
    override def config: Option[Config] = Some(ConfigFactory.load())
  }
}
