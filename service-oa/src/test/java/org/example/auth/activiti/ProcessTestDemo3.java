package org.example.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ProcessTestDemo3 {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    //监听器分配
    @Test
    public void deployProcess02() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban02.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void startProcessInstance02() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban02");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    //uel-method实现
    @Test
    public void deployProcess01() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban01.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void startProcessInstance01() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban04");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }


    //uwl-value实现
    //1. 部署流程定义
    @Test
    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban04.bpmn20.xml")
                .name("加班申请流程04").deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    //2. 查询组任务
    @Test
    public void findGroupTaskList() {
        List<Task> list = taskService.createTaskQuery()
                .taskCandidateUser("tom01")
                .list();
        for(Task task: list) {
            System.out.println("流程实例id"+ task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    //3.拾取组任务
    @Test
    public void claimTask() {
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("tom01")
                .singleResult();
        if(task != null) {
            taskService.claim(task.getId(),"tom01");
            System.out.println("任务拾取完成");
        }
    }

    @Test
    public void findTaskList() {
        String assignee  = "tom01";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee).list();
        for(Task task:list) {
            System.out.println("流程实例id"+ task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("tom01")  //要查询的负责人
                .singleResult();//返回一条
        //完成任务,参数：任务id
        taskService.complete(task.getId());
    }

    @Test
    public void startProcessInstance() {
        Map<String,Object> map = new HashMap<>();
        map.put("assignee1","lucy03");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }


}
