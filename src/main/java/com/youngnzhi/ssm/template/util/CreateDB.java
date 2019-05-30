package com.youngnzhi.ssm.template.util;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.springframework.web.context.ContextLoader;

/**
 * @author: youngnzhi
 * @description: 用Java程序创建activiti数据库表
 * @date: 2019/5/29
 */
public class CreateDB {
    public static void main(String[] args) {
        String resource = "spring/spring-activiti.xml";

        /**
         创建一个ProcessEngineConfiguration对象
         此方法从 activiti.cfg.xml 中找固定的ProcessEngineConfiguration
         */
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource(resource);

        //通过ProcessEngineConfiguration创建processEngine对象
        ProcessEngine processEngine = configuration.buildProcessEngine();
        System.out.println(processEngine);
    }
}
