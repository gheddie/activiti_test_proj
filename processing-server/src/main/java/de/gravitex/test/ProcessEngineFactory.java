package de.gravitex.test;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public class ProcessEngineFactory {

	public static ProcessEngine getDefaultProcessEngine() {
		/*
		return ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
				  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
				  .setJdbcUrl("jdbc:jtds:sqlserver://bcc-sql08-demo:1433/coredb_processing")
				  .setJdbcDriver("net.sourceforge.jtds.jdbc.Driver")
				  .setJdbcUsername("coredb")
				  .setJdbcPassword("coredb")
				  .setJobExecutorActivate(false)
				  .setDatabaseSchemaUpdate("true")
				  .buildProcessEngine();
				  */
		//---
		return ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
				  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
				  .setJdbcUrl("jdbc:postgresql://10.207.1.106/activiti_test")
//				  .setJdbcUrl("jdbc:postgresql://localhost/activiti_test")
				  .setJdbcUsername("postgres")
				  .setJdbcPassword("pgvedder")
				  .setJobExecutorActivate(false)
				  .setDatabaseSchemaUpdate("true")
				  .buildProcessEngine();
	}
}
