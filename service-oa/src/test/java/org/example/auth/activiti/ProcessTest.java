package org.example.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;


@SpringBootTest
public class ProcessTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    //单个挂起
    @Test
    public void SingleSuspendProcessInstance() {
        String processInstanceId = "0f2a8376-9f48-11ee-a5d9-0a002700000c";
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取到当前流程定义是否为暂停状态   suspended方法为true代表为暂停   false就是运行的
        boolean suspended = processInstance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            System.out.println("流程实例:" + processInstanceId + "挂起");
        }
    }
    //全部流程挂起
    @Test
    public void suspendProcessInstanceALl() {
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("qingjia").singleResult();
        boolean suspended = qingjia.isSuspended();
        if(suspended) {
            repositoryService.activateProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println(qingjia.getId() + "激活了");
        }else {
            repositoryService.suspendProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println(qingjia.getId()+"挂起了");
        }
    }

    @Test
    public void startUpProcessAddBusinessKey() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia", "1001");
        System.out.println(processInstance.getBusinessKey());
    }

    @Test
    public void findCompleteTaskList() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee("zhangsan")
                .finished().list();
        for(HistoricTaskInstance historicTaskInstance:list) {
            System.out.println("实例id："+historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id：" + historicTaskInstance.getId());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务名称：" + historicTaskInstance.getName());
        }
    }

    @Test
    public void completeTask() {
        Task task = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .singleResult();
        taskService.complete(task.getId());
    }

    @Test
    public void findTaskList() {
        String assignee  = "zhangsan";
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
    public void startProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia");
        System.out.println("流程定义id" + processInstance.getProcessInstanceId());
        System.out.println("流程实例id" + processInstance.getId());
        System.out.println("当前活动id" + processInstance.getActivityId());
    }


    @Test
    public void deployProcess() {
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia1.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }
}
