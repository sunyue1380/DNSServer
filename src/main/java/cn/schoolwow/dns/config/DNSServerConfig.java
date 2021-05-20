package cn.schoolwow.dns.config;

import cn.schoolwow.dns.constants.DictionaryKey;
import cn.schoolwow.dns.entity.DNSRecursionServer;
import cn.schoolwow.dns.entity.Dictionary;
import cn.schoolwow.quickdao.QuickDAO;
import cn.schoolwow.quickdao.dao.DAO;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ComponentScan(basePackages = "cn.schoolwow")
public class DNSServerConfig {
    @Bean
    @Autowired
    public DAO quickdao(HikariDataSource hikariDataSource){
        DAO dao = QuickDAO.newInstance()
                .dataSource(hikariDataSource)
                .packageName("cn.schoolwow.dns.entity")
                .autoCreateTable(true)
                .build();
        //初始化字典表值
        DictionaryKey[] dictionaryKeys = DictionaryKey.values();
        for (DictionaryKey dictionaryKey : dictionaryKeys) {
            Dictionary dictionary = new Dictionary(dictionaryKey.name(),dictionaryKey.defaultValue,dictionaryKey.description);
            dao.insertIgnore(dictionary);
        }
        DNSRecursionServer dnsRecursionServer = new DNSRecursionServer();
        dnsRecursionServer.setDnsServerIP("223.5.5.5");
        dnsRecursionServer.setEnable(true);
        dnsRecursionServer.setOrder(1);
        dao.insertIgnore(dnsRecursionServer);
        return dao;
    }

    @Bean
    public ThreadPoolExecutor dnsResolverThreadPool(){
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        return threadPoolExecutor;
    }
}
