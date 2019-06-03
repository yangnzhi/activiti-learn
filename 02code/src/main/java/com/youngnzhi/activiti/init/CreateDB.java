package com.youngnzhi.activiti.init;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

/**
 * @author: youngnzhi
 * @description: 创建activiti数据库表有两种方式:
 *      1,使用activiti文件中包含的创建数据库表的脚本直接创建数据库表
 *      2,用Java程序创建activiti数据库表
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
        System.out.println("====用Java程序创建activiti数据库表完成===");
    }
}
