package com.customerService.app.config;


import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class ActivitiConfig {


    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager txManager) {
        SpringProcessEngineConfiguration speconfig = new SpringProcessEngineConfiguration();
        speconfig.setDataSource(dataSource);
        speconfig.setTransactionManager(txManager);
        speconfig.setDatabaseSchemaUpdate("true");
        Resource[] resources = new Resource[1];
        resources[0] = new ClassPathResource("process/Facility.bpmn20.xml");
        speconfig.setDeploymentResources(resources);
        return speconfig;
    }


    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration spec) {
        ProcessEngineFactoryBean pefbean = new ProcessEngineFactoryBean();
        pefbean.setProcessEngineConfiguration(spec);
        return pefbean;

    }

    @Bean
    public RepositoryService repositoryService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getRuntimeService();
    }

    @Bean
    public HistoryService historyService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getHistoryService();
    }

    @Bean
    public ManagementService managementService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getManagementService();
    }

    @Bean
    public IdentityService identityService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getIdentityService();
    }

    @Bean
    public FormService formService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getFormService();
    }

    @Bean
    public TaskService taskService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getTaskService();
    }
}
