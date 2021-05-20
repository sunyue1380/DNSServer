package cn.schoolwow.dns;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import cn.schoolwow.dns.config.ConfigurationFile;
import cn.schoolwow.dns.config.DNSServerConfig;
import cn.schoolwow.quickserver.QuickServer;
import org.aeonbits.owner.ConfigCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {
        //指定类型和路径
        loadLogbackConfig();

        int port = 9999;
        String profile = System.getProperty("spring.profiles.active");
        if("dev".equals(profile)){
            QuickServer.newInstance()
                    .port(port)
                    .staticResourcePath(System.getProperty("user.dir")+"/src/main/resources")
                    .staticResourcePathPrefix("/static")
                    .register(DNSServerConfig.class)
                    .keepAlive(false)
                    .start();
        }else if("prod".equals(profile)){
            QuickServer.newInstance()
                    .port(port)
                    .staticResourcePathPrefix("/static")
                    .register(DNSServerConfig.class)
                    .keepAlive(false)
                    .start();
        }else if("release".equals(profile)){
            ConfigurationFile configurationFile = ConfigCache.getOrCreate(ConfigurationFile.class);
            QuickServer.newInstance()
                    .port(configurationFile.port())
                    .staticResourcePathPrefix("/static")
                    .register(DNSServerConfig.class)
                    .keepAlive(false)
                    .start();
        }else{
            throw new RuntimeException("profile属性未设置或者不合法!profile:"+profile);
        }
    }

    /**
     * 加载当前路径下的日志配置文件
     */
    private static void loadLogbackConfig() {
        File logbackFile = new File(System.getProperty("user.dir") + "/logback.xml");
        logger.info("[准备加载日志配置文件]是否存在:{},路径:{}",logbackFile.exists(),logbackFile.getAbsolutePath());
        if (logbackFile.exists()) {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            try {
                configurator.doConfigure(logbackFile);
            } catch (JoranException e) {
                e.printStackTrace(System.err);
            }
        }
   }
}
