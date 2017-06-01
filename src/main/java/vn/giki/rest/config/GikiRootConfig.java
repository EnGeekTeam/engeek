package vn.giki.rest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:datasource.properties")
public class GikiRootConfig {
	private static final String MYSQL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private Environment env;

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Bean
	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName(env.getRequiredProperty("driver_class"));
		Connection conn = DriverManager.getConnection(env.getRequiredProperty("url"), env.getRequiredProperty("user"),
				env.getRequiredProperty("password"));
		
		return conn;
	}

	@Bean
	public SimpleDateFormat dateTimeFormat() {
		return new SimpleDateFormat(MYSQL_DATE_TIME_FORMAT);
	}
}
