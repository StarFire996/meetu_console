<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table class="easyui-datagrid" id="itemList" title="帖子列表" 
       data-options="singleSelect:false,collapsible:true,pagination:true,url:'/console/post/getImg',method:'get',pageSize:30,toolbar:toolbar">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:60">帖子id</th>
            <th data-options="field:'content',width:200">帖子内容</th>
            <th data-options="field:'pic1',width:50,align:'center',formatter:TAOTAO.formatUrl">图片1</th>
            <th data-options="field:'pic2',width:50,align:'center',formatter:TAOTAO.formatUrl">图片2</th>
            <th data-options="field:'pic3',width:50,align:'center',formatter:TAOTAO.formatUrl">图片3</th>
            <th data-options="field:'pic4',width:50,align:'center',formatter:TAOTAO.formatUrl">图片4</th>
            <th data-options="field:'pic5',width:50,align:'center',formatter:TAOTAO.formatUrl">图片5</th>
            <th data-options="field:'pic6',width:50,align:'center',formatter:TAOTAO.formatUrl">图片6</th>
            <th data-options="field:'pic7',width:50,align:'center',formatter:TAOTAO.formatUrl">图片7</th>
            <th data-options="field:'pic8',width:50,align:'center',formatter:TAOTAO.formatUrl">图片8</th>
            <th data-options="field:'pic9',width:50,align:'center',formatter:TAOTAO.formatUrl">图片9</th>
            <th data-options="field:'commentNum',width:60">评价数</th>
            <th data-options="field:'praiseNum',width:60">点赞数</th>
            <th data-options="field:'level',width:60">优先级</th>
            <th data-options="field:'createBy',width:60">发帖人id</th>
            <th data-options="field:'createAt',width:130,align:'center',formatter:TAOTAO.formatDateTime">发帖日期</th>
        </tr>
    </thead>
</table>
<div id="itemEditWindow" class="easyui-window" title="编辑帖子" data-options="modal:true,closed:true,iconCls:'icon-save',href:'/page/community/post-edit'" style="width:80%;height:80%;padding:10px;">
</div>
<script>

    function getSelectionsIds(){
    	//获取id为itemList元素，就是获取datagrid
    	var itemList = $("#itemList");
    	//获取选择的数据
    	var sels = itemList.datagrid("getSelections");
    	var ids = [];
    	//遍历选择的数据
    	for(var i in sels){
    		//把数据中的id放到ids数组中
    		ids.push(sels[i].id);
    	}
    	//把ids数组转为字符串，多个值中间用“，”分隔
    	ids = ids.join(",");
    	return ids;
    }
    
    var toolbar = [{
        text:'编辑',
        iconCls:'icon-edit',
        handler:function(){
        	//执行以上逻辑，获取字符串。
        	var ids = getSelectionsIds();
        	//如果字符串长度为0，其实就是空
        	if(ids.length == 0){
        		$.messager.alert('提示','必须选择一个帖子才能编辑!');
        		return ;
        	}
        	//获取“，”的索引地址
        	//如果只选择了一条数据，就没有“，”，会返回-1
        	//如果选择了两条或两条以上的数据，肯定有“，”，返回的索引肯定是大于0
        	if(ids.indexOf(',') > 0){
        		$.messager.alert('提示','只能选择一个帖子!');
        		return ;
        	}
        	
        	//执行选择了一条数据，才会执行以下逻辑
        	
        	//获取id为itemEditWindow的元素，这个元素有属性,href:'/rest/page/item-edit'" 
        	//其实打开窗口的内容就是访问了这个地址/rest/page/item-edit，加载item-edit.jsp这个页面
        	$("#itemEditWindow").window({
        		//窗口打开成功，执行以下逻辑
        		onLoad :function(){
        			//回显商品基本数据数据
        			//获取选中的数据
        			var data = $("#itemList").datagrid("getSelections")[0];
        			//给id为itemeEditForm加载数据，其实就是商品修改页面的form表单
        			$("#itemeEditForm").form("load",data);
        			//回显没有字段的数据（商品描述）
        			// 加载商品描述
        			//发起/console/post/getPost/{id}的请求，获取数据,其实获取的是商品描述数据
        			//$.getJSON('/console/post/getPost/'+data.id,function(_data){
        			// });
        			      
        		}
        	}).window("open");
        }
    },{
        text:'删除',
        iconCls:'icon-cancel',
        handler:function(){
        	var ids = getSelectionsIds();
        	if(ids.length == 0){
        		$.messager.alert('提示','未选中帖子!');
        		return ;
        	}
        	$.messager.confirm('确认','确定删除ID为 '+ids+' 的帖子吗？',function(r){
        	    if (r){
        	    	var params = {"ids":ids};
                	$.post("/console/post/deletePost",params, function(data){
            			if(data == 200){
            				$.messager.alert('提示','删除帖子成功!',undefined,function(){
            					$("#itemList").datagrid("reload");
            				});
            			}
            		});
        	    }
        	});
        }
    }];
</script>