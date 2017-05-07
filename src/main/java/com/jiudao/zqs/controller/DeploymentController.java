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
 * 部署流程
 * 
 * @author zqs
 */
@Controller
@RequestMapping(value = "/zqs")
public class DeploymentController {
	   protected Logger logger = LoggerFactory.getLogger(getClass());
   

    /**
     * 流程定义列表
     */
    @Autowired
    RepositoryService repositoryService; 
    @RequestMapping(value = "/process-list")
    public ModelAndView processList() {

        // 对应WEB-INF/jsp/zqs/process-list.jsp
        ModelAndView mav = new ModelAndView("zqs/process-list");
        //查询所有的流程 （有的没部署 就不能删除）
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        mav.addObject("processDefinitionList", processDefinitionList);
        return mav;
    }

    /**
     * 部署流程资源   （以web方式文件上传)
     */
    @RequestMapping(value = "/deploy")
    public String deploy(@RequestParam(value = "file", required = true) MultipartFile file) {
        // 获取上传的文件名
        String fileName = file.getOriginalFilename();
        try {
            // 获得输入流
            InputStream fileInputStream = file.getInputStream();
            // 文件的后缀
            String extension = FilenameUtils.getExtension(fileName);
            // zip或者bar类型的文件用ZipInputStream方式部署
            DeploymentBuilder deployment = repositoryService.createDeployment();
            if ("zip".equals(extension) || "bar".equals(extension)) {
                deployment.addZipInputStream(new ZipInputStream(fileInputStream));
            } else {
                // 其他类型的文件直接部署
                deployment.addInputStream(fileName, fileInputStream);
            }
            deployment.deploy();
        } catch (Exception e) {
            logger.error("出错啦");
        }
        return "redirect:process-list";
    }

    /**
     * 删除部署的流程
     *
     * @param deploymentId 流程部署ID
     */
    @RequestMapping(value = "/delete-deployment")
    public String deleteProcessDefinition(@RequestParam("deploymentId") String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
        return "redirect:process-list";
    }

    /**
     * 读取流程资源
     *
     * @param processDefinitionId 流程定义ID
     * @param resourceName        资源名称
     */
    @RequestMapping(value = "/read-resource")
    public void readResource(@RequestParam("pdid") String processDefinitionId, @RequestParam("resourceName") String resourceName, HttpServletResponse response)
            throws Exception {
    	//获取流程定义对象
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        // 获取资源输入流
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }


}
