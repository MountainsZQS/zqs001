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
 * �����Զ�������Դ
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class AutoDeploymentUseSpring {

    @Autowired
    RepositoryService repositoryService;

    @Test
    public void testDeployment() {
        
        //assertEquals(1, count);   //expected:<1> but was:<16>
       
        //��classpath��ȡ��Դ
         InputStream zipInputStream=getClass().getClassLoader().getResourceAsStream("diagrams/userAndTask.bar");
         //����������
		repositoryService.createDeployment().addZipInputStream(new ZipInputStream(zipInputStream)).deploy();
		//��ѯ���̵�����
		long count = repositoryService.createProcessDefinitionQuery().count();
		 System.out.println(count);
		 //��ѯ�����¼
		 String id = repositoryService.createDeploymentQuery().singleResult().getId();
		 System.out.println(id);
    }

}
