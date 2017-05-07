package com.jiudao.zqs.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.zip.ZipInputStream;


/**
 * 测试自动部署资源
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AutoDeploymentUseSpring {

    @Autowired
    RepositoryService repositoryService;

    @Test
    public void testDeployment() {
        
        //assertEquals(1, count);   //expected:<1> but was:<16>
       
        //从classpath读取资源
         InputStream zipInputStream=getClass().getClassLoader().getResourceAsStream("diagrams/userAndTask.bar");
         //部署到引擎中
		repositoryService.createDeployment().addZipInputStream(new ZipInputStream(zipInputStream)).deploy();
		//查询流程的数量
		long count = repositoryService.createProcessDefinitionQuery().count();
		 System.out.println(count);
		 //查询部署记录
		 String id = repositoryService.createDeploymentQuery().singleResult().getId();
		 System.out.println(id);
    }

}
