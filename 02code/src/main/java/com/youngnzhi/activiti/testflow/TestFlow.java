package com.youngnzhi.activiti.testflow;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author yangnzhi
 * @date&time 2019/5/31
 * @description activiti入门体验
 **/
public class TestFlow {

    //流程定义的key，创建流程时设置的流程id
    private String Process_Key = "purchasingflow";

    /**
     * ProcessEngineConfiguration加载默认的activiti配置文件生成ProcessEngine
     * 默认的activiti名称为 activiti.cfg.xml且一定位于classpath下，不能位于classpath二级目录下
    */
    private ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();

    /**
     * 部署流程定义
     * 使用RepositoryService进行流程定义部署
     * 流程修改后，一定要重新部署流程！！！！
     */
    @Test
    public void processDeployment(){
        //使用RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //加载purchasingflow1.bpmn
        InputStream inputStream_bpmn = this.getClass().getClassLoader().getResourceAsStream("diagram/purchasingflow1.bpmn");
        //加载purchasingflow1.png
        InputStream inputStream_png = this.getClass().getClassLoader().getResourceAsStream("diagram/purchasingflow1.png");

        Deployment deploy = repositoryService.createDeployment()
                                .addInputStream("purchasingflow1.bpmn", inputStream_bpmn)
                                .addInputStream("purchasingflow1.png", inputStream_png)
                                .deploy();

        System.out.println("部署id：" + deploy.getId());
        System.out.println("部署时间：" + deploy.getDeploymentTime());
    }

    /**
     * 启动流程定义
     * 由参与者发起一个流程，使用runTimeService
     */
    @Test
    public void startProcessInstance(){
        //创建runtimeservice
        RuntimeService runtimeService = processEngine.getRuntimeService();

        //根据流程定义的key去启动流程，activiti找到最新版本的流程定义bpmn文件去启动
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(this.Process_Key);

        System.out.println("流程实例的id: " + processInstance.getProcessInstanceId());
        System.out.println("流程定义的id: " + processInstance.getId());
        System.out.println("流程当前运行节点标识: " + processInstance.getActivityId());
    }

    /**
     * 查询待办任务
     * 使用TaskService查询当前待办任务
     */
    @Test
    public void queryTaskList(){
        //任务负责人
        String assignee = "xiaoli";

        //使用taskservice
        TaskService taskService = processEngine.getTaskService();

        //任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();

        //设置查询条件
        //指定要查询的流程key
        taskQuery.processDefinitionKey(this.Process_Key);
        //指定流程任务的负责人
        taskQuery.taskAssignee(assignee);

        //查询任务列表
        List<Task> taskList = taskQuery.list();
        for (Task task : taskList){
            System.out.println("任务id：" + task.getId());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 办理任务
     */
    @Test
    public void completeTask(){
        //任务id
        String taskId = "25002";

        //使用taskservice
        TaskService taskService = processEngine.getTaskService();

        taskService.complete(taskId);
        System.out.println("完成任务！！！");
    }
}
