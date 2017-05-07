<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> --%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<!-- 上传 -->
	<form action="${ctx }/zqs/deploy" method="post" enctype="multipart/form-data" style="margin-top:1em;">
			<input type="file" name="file" />
			<input type="submit" value="部署资源" class="btn" />
		</form>
		
<!-- 展示 -->	
	<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th>流程定义ID</th>
				<th>部署ID</th>
				<th>流程定义名称</th>
				<th>流程定义KEY</th>
				<th>版本号</th>
				<th>XML资源名称</th>
				<th>图片资源名称</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${processDefinitionList }" var="pd">
				<tr>
					<td>${pd.id }</td>
					<td>${pd.deploymentId }</td>
					<td>${pd.name }</td>
					<td>${pd.key }</td>
					<td>${pd.version }</td>
					
					<td><a  href="${ctx }/zqs/read-resource?pdid=${pd.id }&resourceName=${pd.resourceName }">${pd.resourceName }</a></td>
					<td><a  href="${ctx }/zqs/read-resource?pdid=${pd.id }&resourceName=${pd.diagramResourceName }">${pd.diagramResourceName }</a></td>
					<td><a  href="${ctx }/zqs/delete-deployment?deploymentId=${pd.deploymentId }">删除</a></td>
							
						
						<%-- <form action="${ctx}/zqs/delete-deployment?deploymentId=${pd.deploymentId }" method="post" >
						<input type="submit" value="删除部署"  />
						<form action="${ctx}/zqs/getform/start/${pd.id } }" method="post" >
						<input type="submit" value="启动"  /> --%>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>