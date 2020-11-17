import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonBean {
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${database.driverClassName}")
    private String driverClassName;
    @Value("${database.initialSize}")
    private int initialSize;
    @Value("${database.maxActive}")
    private int maxActive;
    @Value("${database.maxWait}")
    private int maxWait;
    @Value("${database.validationQuery}")
    private String validationQuery;
    @Value("${database.autoCommit}")
    private boolean autoCommit;
    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setDefaultAutoCommit(autoCommit);
        return dataSource;
    }
}
