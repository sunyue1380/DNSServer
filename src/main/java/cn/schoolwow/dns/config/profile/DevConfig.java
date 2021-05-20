package cn.schoolwow.dns.config.profile;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {
    @Bean
    public HikariDataSource hikariDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mariadb://127.0.0.1:3306/dnsserver");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }
}
