package com.rfchina.wallet.server.config;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = {"com.rfchina.wallet.domain.mapper","com.rfchina.wallet.server.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")

@Slf4j
public class DataSourceConfig {

	private static final String MAPPER_LOCATION = "classpath*:sqlmap/**/*.xml";

	@Bean(name = "dataSource")
	public DataSource dataSource(
		@Value("${jdbc.driverClassName}") String driverClassName,
		@Value("${jdbc.url}") String jdbcUrl,
		@Value("${jdbc.username}") String username,
		@Value("${jdbc.password}") String password
	) {
		return DataSourceBuilder.create()
			.driverClassName(driverClassName)
			.url(jdbcUrl)
			.username(username)
			.password(password)
			.build();
	}

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
		throws Exception {
		VFS.addImplClass(SpringBootVFS.class);

		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver()
			.getResources(MAPPER_LOCATION));

		return bean.getObject();
	}

	@Bean(name = "transactionManager")
	public DataSourceTransactionManager transactionManager(
		@Qualifier("dataSource") DataSource dataSource) {

		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlSessionTemplate")
	public SqlSessionTemplate ncSqlSessionTemplate(
		@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {

		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
