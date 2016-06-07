<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="/js/kindeditor-4.1.10/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="/js/kindeditor-4.1.10/lang/zh_CN.js"></script>
<div style="padding:10px 10px 10px 10px">
	<form id="itemeEditForm" class="itemForm" method="post">
		<input type="hidden" name="id"/>
	    <table cellpadding="5">
	        <tr>
	            <td>话题优先级:</td>
	            <td><input class="easyui-textbox" type="text" data-options="required:true" style="width: 100px;" name="level"></input></td>
	        </tr>
	       
	    </table>
	    <input type="hidden" name="id"/>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()">提交</a>
	</div>
</div>
<script type="text/javascript">
	var itemEditEditor ;
	$(function(){
		//实例化编辑器
		itemEditEditor = TAOTAO.createEditor("#itemeEditForm [name=desc]");
	});
	
	function submitForm(){
		if(!$('#itemeEditForm').form('validate')){
			$.messager.alert('提示','表单还未填写完成!');
			return ;
		}
		//将编辑器中的内容同步到隐藏的多行文本中
		//itemEditEditor.sync();
				
		//提交到后台的RESTful
		$.ajax({
		   type: "POST",
		   url: "/console/tag/updateTag",
		   data: $("#itemeEditForm").serialize(),
		   success: function(msg){
			   $.messager.alert('提示','修改话题成功!','info',function(){
				   //关闭修改窗口
					$("#itemEditWindow").window('close');
				   //重载datagrid
					$("#itemList").datagrid("reload");
				});
		   },
		   error: function(){
			   $.messager.alert('提示','修改话题失败!');
		   }
		});
	}
</script>
