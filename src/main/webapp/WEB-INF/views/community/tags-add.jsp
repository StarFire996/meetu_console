<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="itemAddForm" class="itemForm" method="post">
	    <table cellpadding="5">
	        <tr>
	            <td>话题内容:</td>
	            <td><input class="easyui-textbox" type="text" name="content" data-options="required:true" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>话题优先级:</td>
	            <td><input class="easyui-textbox" name="level" data-options="required:true" style="width: 100px;"></input></td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="clearForm()">重置</a>
	</div>
</div>
<script type="text/javascript">
	
	function submitForm(){
		//校验表单
		if(!$('#itemAddForm').form('validate')){
			$.messager.alert('提示','表单还未填写完成!');
			return ;
		}
			
		//提交到后台的RESTful
		$.ajax({
		   type: "POST",
		   url: "/console/tag/addTag",
		   //把from表单序列化
		   data: $("#itemAddForm").serialize(),
		   success: function(msg){
			   $.messager.alert('提示','新增话题成功!','info',function(){
				   //关闭修改窗口
					$("#itemAddWindow").window('close');
				   //重载datagrid
					$("#itemList").datagrid("reload");
				});
		   },
		   error: function(){
			   $.messager.alert('提示','新增话题失败!');
		   }
		});
	}
	
	function clearForm(){
		$('#itemAddForm').form('reset');
		itemAddEditor.html('');
	}
</script>
