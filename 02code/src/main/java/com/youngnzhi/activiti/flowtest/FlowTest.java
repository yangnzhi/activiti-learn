package com.youngnzhi.activiti.flowtest;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @author yangnzhi
 * @date&time 2019/5/31
 * @description activiti入门体验
 **/
public class FlowTest {

    private final Logger logger = Logger.getLogger(FlowTest.class);

    //流程定义的key也就是创建流程时设置的流程id
    private static final String PROCESS_DEFINITION_KEY = "purchasingflow";
    //具体的流程定义的id
    private static final String PROCESS_DEFINITION_ID = "purchasingflow:5:32504";
    //流程部署id
    private static final String PROCESS_DEPLOYMENT_ID = "30001";
    //文件保存路径
    private static final String RESOUCRE_PATH = "F:/workspace/appData/activiti/";

    /**
     * 方式一：
     * ProcessEngines加载默认的activiti配置文件生成ProcessEngine
     * 默认的activiti名称为 activiti.cfg.xml且一定位于classpath下，不能位于classpath二级目录下
    */
    //private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 方式二：
     * ProcessEngineConfiguration加载配置文件生成processEngine
     * ProcessEngineConfiguration获取的默认对象id为 processEngineConfiguration，是在配置文件定义的
     * 如果配置文件中定义的不为默认id，则使用 ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(resource,id).buildProcessEngine();
     */
    String resource = "activiti.cfg.xml";
    private ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(resource).buildProcessEngine();

    /**
     * 部署流程定义
     * 方法1:单个文件部署
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
                                //.addInputStream("purchasingflow2.bpmn", inputStream_bpmn2) 一次可以部署多个流程
                                //.addInputStream("purchasingflow2.png", inputStream_png2)
                                .deploy();

        System.out.println("部署id：" + deploy.getId());
        System.out.println("部署时间：" + deploy.getDeploymentTime());
    }

    /**
     * 部署流程定义
     * 方法2:zip压缩包部署 将purchasingflow1.bpmn和purchasingflow1.png压缩成一个zip包
     * 使用RepositoryService进行流程定义部署
     * 流程修改后，一定要重新部署流程！！！！
     */
    @Test
    public void processDeploymentByZip(){
        //使用RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //定义zip输入流
        InputStream inputStream_zip = this.getClass().getClassLoader().getResourceAsStream("diagram/purchasingflow1.zip");
        ZipInputStream zipInputStream = new ZipInputStream(inputStream_zip);

        //部署zip输入流
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();

        System.out.println("部署id：" + deploy.getId());
        System.out.println("部署时间：" + deploy.getDeploymentTime());
    }

    /**
     * 流程定义查询
     * 通过此功能查询本系统通过actviti管理的流程有哪些
     * 使用repositoryService查询流程定义，可以根据流程定义的key查询某个业务流程在activiti中的流程定义
     */
    @Test
    public void queryProcessDefinition(){

        //使用repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //流程定义查询对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

        //设置查询条件，根据流程定义key查询多个流程
        processDefinitionQuery.processDefinitionKey(PROCESS_DEFINITION_KEY);

        //得出查询列表
        List<ProcessDefinition> list =  processDefinitionQuery.list();
        for(ProcessDefinition processDefinition:list){
            System.out.println("--------------------------------");
            System.out.println("流程定义的id："+processDefinition.getId());
            System.out.println("流程定义的名称："+processDefinition.getName());
            System.out.println("流程定义的key："+processDefinition.getKey());
            System.out.println("流程部署id："+processDefinition.getDeploymentId());
            System.out.println("bpmn文件名："+processDefinition.getResourceName());
            System.out.println("png文件名："+processDefinition.getDiagramResourceName());
        }
    }

    /**
     * 流程定义的资源文件查询
     * 需求：查看activiti中某个流程定义的资源文件（.bpmn和.png），程序员需要查看bpmn（xml格式），终端用户查询图片。
     */
    @Test
    public void getProcessResources() {

        //使用repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //查询一个流程定义,根据流程定义id查询
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                                                      .processDefinitionId(PROCESS_DEFINITION_ID)
                                                      .singleResult();

        //bpmn文件名称
        String bpmn_name = processDefinition.getResourceName();
        //png图片名称
        String png_name = processDefinition.getDiagramResourceName();
        //部署id，来源于流程部署表
        String deploymentId = processDefinition.getDeploymentId();

        // 资源文件名称
        String resourceName_bpmn = bpmn_name;
        String resourceName_png = png_name;

        //获取bpmn文件输入流
        InputStream inputStream_bpmn = repositoryService.getResourceAsStream(deploymentId, resourceName_bpmn);
        InputStream inputStream_png = repositoryService.getResourceAsStream(deploymentId, resourceName_png);

        //将输入流通过文件输出到本地磁盘保存
        FileOutputStream fileOutputStream_bpmn = null;
        FileOutputStream fileOutputStream_png = null;

        try{
            fileOutputStream_bpmn = new FileOutputStream(new File(RESOUCRE_PATH + resourceName_bpmn));
            fileOutputStream_png = new FileOutputStream(new File(RESOUCRE_PATH + resourceName_png));

            byte[] bytes = new byte[1024];
            int len = -1;
            while ((len = inputStream_bpmn.read(bytes,0,1024))!= -1){
                fileOutputStream_bpmn.write(bytes,0,len);
            }
            while ((len = inputStream_png.read(bytes,0,1024))!= -1){
                fileOutputStream_png.write(bytes,0,len);
            }
        } catch (Exception e) {
            logger.info("文件保存失败 ",e);
        } finally {
            if(null != fileOutputStream_bpmn){
                try {
                    fileOutputStream_bpmn.close();
                } catch (IOException e) {
                    logger.info("fileOutputStream_bpmn 释放资源失败 ",e);
                }
            }
            if(null != fileOutputStream_png){
                try {
                    fileOutputStream_png.close();
                } catch (IOException e) {
                    logger.info("fileOutputStream_png 释放资源失败 ",e);
                }
            }
            if(null != inputStream_bpmn){
                try {
                    inputStream_bpmn.close();
                } catch (IOException e) {
                    logger.info("inputStream_bpmn 释放资源失败 ",e);
                }
            }
            if(null != inputStream_png){
                try {
                    inputStream_png.close();
                } catch (IOException e) {
                    logger.info("inputStream_png 释放资源失败 ",e);
                }
            }
            System.out.println("====保存文件结束====");
        }
    }

    /**
     * 流程定义删除
     * 根据流程部署id删除整个流程
     * 流程一但启动是不删除的，给超级管理开放级联的功能。可以暂停/激活流程的执行
     */
    @Test
    public void deleteProcessDefinition(){
        RepositoryService repositoryService = processEngine.getRepositoryService();

        //如果流程已经启动，该方法无法删除流程
        repositoryService.deleteDeployment(PROCESS_DEPLOYMENT_ID);

        //通过级联删除将流程及相关的所有内容强制删除，无论流程是否启动
        //repositoryService.deleteDeployment(this.PROCESS_DEPLOYMENT_ID,true);
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(PROCESS_DEFINITION_KEY);

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
        String assignee = "zhangsan";

        //使用taskservice
        TaskService taskService = processEngine.getTaskService();

        //任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();

        //设置查询条件
        //指定要查询的流程key
        taskQuery.processDefinitionKey(PROCESS_DEFINITION_KEY);
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
