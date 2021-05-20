package cn.schoolwow.dns.config;

import org.aeonbits.owner.Config;

@Config.Sources({"file:${user.dir}/config.properties"})
public interface ConfigurationFile extends Config {
    @Key("port")
    int port();

    @Key("username")
    @DefaultValue("")
    String username();

    @Key("password")
    @DefaultValue("")
    String password();

    @Key("jdbcUrl")
    String jdbcUrl();

    @Key("db.username")
    String dbUsername();

    @Key("db.password")
    String dbPassword();
}
