package cn.schoolwow.dns.config.profile;

import cn.schoolwow.dns.config.ConfigurationFile;
import com.zaxxer.hikari.HikariDataSource;
import org.aeonbits.owner.ConfigCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Profile("release")
public class ReleaseConfig {
    @Bean
    public HikariDataSource hikariDataSource(){
        ConfigurationFile configurationFile = ConfigCache.getOrCreate(ConfigurationFile.class);
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setJdbcUrl(configurationFile.jdbcUrl());
        dataSource.setUsername(configurationFile.dbUsername());
        dataSource.setPassword(configurationFile.dbPassword());
        return dataSource;
    }
}
