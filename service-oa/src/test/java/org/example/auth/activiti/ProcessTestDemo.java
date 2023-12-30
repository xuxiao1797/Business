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
public class ProcessTestDemo {

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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban01");
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }


    //uwl-value实现
    //部署流程定义
    @Test
    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .addClasspathResource("process/jiaban.png")
                .name("加班申请流程").deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void startProcessInstance() {
        Map<String,Object> map = new HashMap<>();
        map.put("assignee1","lucy");
        map.put("assignee2","mary");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiaban", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    @Test
    public void findTaskList() {
        String assignee  = "jack";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee).list();
        for(Task task:list) {
            System.out.println("流程实例id"+ task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }
}
