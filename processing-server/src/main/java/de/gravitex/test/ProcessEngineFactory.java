package de.gravitex.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public class ProcessEngineFactory {

	public static ProcessEngine getDefaultProcessEngine() {
		return ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
				  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
				  .setJdbcUrl("jdbc:postgresql://localhost/activiti_test")
				  .setJdbcUsername("postgres")
				  .setJdbcPassword("pgvedder")
				  .setJobExecutorActivate(false)
				  .setDatabaseSchemaUpdate("true")
				  .buildProcessEngine();		
	}
}
