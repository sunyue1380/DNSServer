package cn.schoolwow.dns.config.profile;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Profile("prod")
public class ProdConfig {
    @Bean
    public HikariDataSource hikariDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mariadb://127.0.0.1:3306/dns");
        dataSource.setUsername("dns");
        dataSource.setPassword("123456");
        return dataSource;
    }
}
