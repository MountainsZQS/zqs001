package com.jiudao.zqs.controller;



import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * ��������
 * 
 * @author zqs
 */
@Controller
@RequestMapping(value = "/zqs")
public class DeploymentController {
	   protected Logger logger = LoggerFactory.getLogger(getClass());
   

    /**
     * ���̶����б�
     */
    @Autowired
    RepositoryService repositoryService; 
    @RequestMapping(value = "/process-list")
    public ModelAndView processList() {

        // ��ӦWEB-INF/jsp/zqs/process-list.jsp
        ModelAndView mav = new ModelAndView("zqs/process-list");
        //��ѯ���е����� ���е�û���� �Ͳ���ɾ����
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        mav.addObject("processDefinitionList", processDefinitionList);
        return mav;
    }

    /**
     * ����������Դ   ����web��ʽ�ļ��ϴ�)
     */
    @RequestMapping(value = "/deploy")
    public String deploy(@RequestParam(value = "file", required = true) MultipartFile file) {
        // ��ȡ�ϴ����ļ���
        String fileName = file.getOriginalFilename();
        try {
            // ���������
            InputStream fileInputStream = file.getInputStream();
            // �ļ��ĺ�׺
            String extension = FilenameUtils.getExtension(fileName);
            // zip����bar���͵��ļ���ZipInputStream��ʽ����
            DeploymentBuilder deployment = repositoryService.createDeployment();
            if ("zip".equals(extension) || "bar".equals(extension)) {
                deployment.addZipInputStream(new ZipInputStream(fileInputStream));
            } else {
                // �������͵��ļ�ֱ�Ӳ���
                deployment.addInputStream(fileName, fileInputStream);
            }
            deployment.deploy();
        } catch (Exception e) {
            logger.error("������");
        }
        return "redirect:process-list";
    }

    /**
     * ɾ�����������
     *
     * @param deploymentId ���̲���ID
     */
    @RequestMapping(value = "/delete-deployment")
    public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return "redirect:process-list";
    }

    /**
     * ��ȡ������Դ
     *
     * @param processDefinitionId ���̶���ID
     * @param resourceName        ��Դ����
     */
    @RequestMapping(value = "/read-resource")
    public void readResource(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName, HttpServletResponse response)
            throws Exception {
    	//��ȡ���̶������
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        // ��ȡ��Դ������
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);
        // �����Դ���ݵ���Ӧ����
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }


}
