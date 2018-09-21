package io.pivotal.pad.cronos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import com.datastax.driver.core.Session;

@Configuration
@EnableAutoConfiguration
@Profile(value = "cloud")
class CassandraCloudConfig {

	@Configuration
	@EnableCassandraRepositories(basePackages = { "io.pivotal.pad.cronos.repo" })
	class CassandraConfig extends AbstractCassandraConfiguration {

		@Autowired
		YugabyteAppProperties properties;

		@Value("${cronos.yugabyte.hostname}")
		private String cassandraHost;

		@Value("${cronos.yugabyte.port:9042}")
		private int cassandraPort;

		@Override
		public String getKeyspaceName() {
			return properties.getKeyspace();
		}

		@Override
		public String getContactPoints() {
			return cassandraHost;

		}

		@Override
		public int getPort() {
			return cassandraPort;
		}

		@Override
		public SchemaAction getSchemaAction() {
			return SchemaAction.CREATE_IF_NOT_EXISTS;
		}

		@Bean
		public CassandraTemplate cassandraTemplate(Session session) {
			return new CassandraTemplate(session);
		}

	}
}