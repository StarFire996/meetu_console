Date.prototype.format = function(format){ 
    var o =  { 
    "M+" : this.getMonth()+1, //month 
    "d+" : this.getDate(), //day 
    "h+" : this.getHours(), //hour 
    "m+" : this.getMinutes(), //minute 
    "s+" : this.getSeconds(), //second 
    "q+" : Math.floor((this.getMonth()+3)/3), //quarter 
    "S" : this.getMilliseconds() //millisecond 
    };
    if(/(y+)/.test(format)){ 
    	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
    for(var k in o)  { 
	    if(new RegExp("("+ k +")").test(format)){ 
	    	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
	    } 
    } 
    return format; 
};

var TT = TAOTAO = {
	// 编辑器参数
	kingEditorParams : {
		filePostName  : "uploadFile",   //上传文件名
		uploadJson : '/rest/pic/upload',//请求路径
		dir : "image"	//文件类型
	},
	// 格式化时间
	formatDateTime : function(val,row){
		var now = new Date(val);
    	return now.format("yyyy-MM-dd hh:mm:ss");
	},
	// 格式化连接
	formatUrl : function(val,row){
		if(val){
			return "<a href='"+val+"' target='_blank'>查看</a>";			
		}
		return "";
	},
	// 格式化价格
	formatPrice : function(val,row){
		return (val/100).toFixed(2);
	},
	// 格式化商品的状态
	formatActivityStatus : function formatStatus(val,row){
        if (val == 0){
            return '新鲜';
        } else if(val == 1){
        	return '推荐';
        } else if(val == 2){
        	return 'biubiu';
        }else {
        	return '未知';
        }
    },
    
    init : function(data){
    	this.initPicUpload(data);
    	this.initItemCat(data);
    },
    // 初始化图片上传组件
    initPicUpload : function(data){
    	//获取class为picFileUpload的元素，实际是获取上传图片按钮
    	//对获取到的按钮进行遍历，i是角标，e是遍历的元素
    	$(".picFileUpload").each(function(i,e){
    		//把遍历的元素转为jQuery对象
    		var _ele = $(e);
    		//siblings寻找同级元素，div标签，class为pics的元素
    		//找到后移除，其实是做清理操作
    		_ele.siblings("div.pics").remove();
    		//在按钮后面增加一个div标签class为pics
    		//在里面增加增加一个ul标签
    		_ele.after('\
    			<div class="pics">\
        			<ul></ul>\
        		</div>');
    		//图片回显
    		//判断data不为空或者为undefined，而且data的pics属性不为空
        	if(data && data.pics){
        		//对pics的值做切分，用“，”切分，得到一个数据imgs
        		var imgs = data.pics.split(",");
        		//遍历该数组
        		for(var i in imgs){
        			//对url去空格
        			if($.trim(imgs[i]).length > 0){
        				//获取上传图片按钮的统计元素class为pics，其实找到的就是上面声明的div标签里面的ul标签
						//在ul里增加li标签,里面有一个a标签，在a标签里面有个img标签
        				_ele.siblings(".pics").find("ul").append("<li><a href='"+imgs[i]+"' target='_blank'><img src='"+imgs[i]+"' width='80' height='50' /></a></li>");
        			}
        		}
        	}
        	//先解绑，再绑定，做清理
        	$(e).unbind('click').click(function(){
        		//parentsUntil:获取符合条件（form）的父元素，但是不包含该匹配的元素
        		//parent("form")找到form元素
        		var form = $(this).parentsUntil("form").parent("form");
        		//加载批量图片上传组件
        		KindEditor.editor(TT.kingEditorParams).loadPlugin('multiimage',function(){
        			//加载的富文本编辑器的上传组件
        			var editor = this;
        			//打开图片上传界面
        			editor.plugin.multiImageDialog({
        				//当点击“全部插入”按钮时，执行clickFn的逻辑
        				//urlListh图片上传成功返回的多个url
						clickFn : function(urlList) {
							_ele.siblings("div.pics").remove();
							_ele.after('\
								<div class="pics">\
									<ul></ul>\
								</div>');
							var imgArray = [];
							//遍历图片url
							KindEditor.each(urlList, function(i, data) {
								//把图片url放到声明的imgArray数组中
								imgArray.push(data.url);
								//找到form元素里的class为pics的ul标签
								//在ul里增加li标签,里面有一个a标签，在a标签里面有个img标签
								//其实就是图片显示，点击图片打开新窗口展示图片
								form.find(".pics ul").append("<li><a href='"+data.url+"' target='_blank'><img src='"+data.url+"' width='80' height='50' /></a></li>");
							});
							//获取form中属性为name=image的元素,把数组转为字符串，中间用“，”分隔。
							form.find("[name=image]").val(imgArray.join(","));
							//关闭上传图片界面
							editor.hideDialog();
						}
					});
        		});
        	});
    	});
    },
    
    // 初始化选择类目组件
    initItemCat : function(data){
    	//class选择器，获取class为selectItemCat的元素，其实就是获取点击按钮，会获取所有的选择类目按钮
    	//i:角标  e:遍历的元素
    	$(".selectItemCat").each(function(i,e){
    		//把元素转换为jQuery对象
    		var _ele = $(e);
    		//类目
    		//判断data不为空而且data.cid也不为空
    		if(data && data.cid){
    			//发起get请求/rest/item/cat/{itemCatId},返回类目数据
				$.getJSON('/rest/item/cat/'+data.cid,function(_data){
       				_ele.after("<span style='margin-left:10px;'>"+_data.name+"</span>");
       			});
    		}else{
    			_ele.after("<span style='margin-left:10px;'></span>");
    		}
    		//先给按钮解绑点击事件，再绑定点击事件，清理原来的点击事件
    		_ele.unbind('click').click(function(){
    			//点击事件
    			//创建了div标签
    			//html("<ul>")在div标签内创建ul标签
    			//.window(*)..window('open'); 打开窗口
    			$("<div>").css({padding:"5px"}).html("<ul>")
    			.window({
    				//创建窗口的参数
    				width:'500',
    			    height:"450",
    			    modal:true,
    			    closed:true,
    			    iconCls:'icon-save',
    			    title:'选择类目',
    			    //当窗口打开后执行
    			    onOpen : function(){
    			    	//_win就是当前窗口
    			    	var _win = this;
    			    	//$("ul")标签选择器，后去ul标签 ，里面的_win,是搜索的范围
    			    	//.tree创建树
    			    	$("ul",_win).tree({
    			    		//创建树发起的请求，获取数据
    			    		//发起请求会携带一个参数名为id的参数，这个参数实际上是改节点，后台逻辑根据改节点id，查询，是子节点的parentid查询
    			    		url:'/rest/item/cat',
    			    		method:'GET',
    			    		animate:true,
    			    		//给树上的所有节点增加点击事件
    			    		onClick : function(node){
    			    			//判断点击的节点是否是叶子节点，如果是叶子节点，就执行
    			    			if($(this).tree("isLeaf",node.target)){
    			    				// 填写到cid中
    			    				//获取按钮的父元素，在父元素中查找name=cid的元素，实际上找到了input标签
    			    				//往input标签内写入节点的id，相当于把类目id填入
    			    				_ele.parent().find("[name=cid]").val(node.id);
    			    				//在按钮下面创建一个文本域，里面写入节点名称，同时增加属性  cid=节点id
    			    				_ele.next().text(node.text).attr("cid",node.id);
    			    				//关闭窗口
    			    				$(_win).window('close');
    			    				//执行传递进来的回调函数（我们没有，所以不管）
    			    				if(data && data.fun){
    			    					data.fun.call(this,node);
    			    				}
    			    			}
    			    		}
    			    	});
    			    },
    			    onClose : function(){
    			    	$(this).window("destroy");
    			    }
    			}).window('open');
    		});
    	});
    },
    
    createEditor : function(select){
    	//KindEditor富文本编辑器对象，create用来创建富文本编辑器
    	//select：是一个字符串#itemAddForm [name=desc]，
    	//其实是id选择器，找到form元素，在里面找name=desc的元素，其实找的是商品描述td多行文本
    	//找到多行文本的用处是在这个位置创建富文本编辑器
    	return KindEditor.create(select, TT.kingEditorParams);
    },
    
    /**
     * 创建一个窗口，关闭窗口后销毁该窗口对象。<br/>
     * 
     * 默认：<br/>
     * width : 80% <br/>
     * height : 80% <br/>
     * title : (空字符串) <br/>
     * 
     * 参数：<br/>
     * width : <br/>
     * height : <br/>
     * title : <br/>
     * url : 必填参数 <br/>
     * onLoad : function 加载完窗口内容后执行<br/>
     * 
     * 
     */
    createWindow : function(params){
    	$("<div>").css({padding:"5px"}).window({
    		width : params.width?params.width:"80%",
    		height : params.height?params.height:"80%",
    		modal:true,
    		title : params.title?params.title:" ",
    		href : params.url,
		    onClose : function(){
		    	$(this).window("destroy");
		    },
		    onLoad : function(){
		    	if(params.onLoad){
		    		params.onLoad.call(this);
		    	}
		    }
    	}).window("open");
    },
    
    closeCurrentWindow : function(){
    	$(".panel-tool-close").click();
    },
    getSelectionsIds : function (select){
    	var list = $(select);
    	var sels = list.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    },
    
    /**
     * 初始化单图片上传组件 <br/>
     * 选择器为：.onePicUpload <br/>
     * 上传完成后会设置input内容以及在input后面追加<img> 
     */
    initOnePicUpload : function(){
    	//给上传图片按钮增加点击事件
    	$(".onePicUpload").click(function(){
    		//把按钮转为jQuery对象
			var _self = $(this);
			//加载单图片上传组件，组件是富文本编辑器的
			KindEditor.editor(TT.kingEditorParams).loadPlugin('image', function() {
				//打开图片上传界面
				this.plugin.imageDialog({
					showRemote : false,
					//点击确定按钮执行clickFn逻辑
					clickFn : function(url, title, width, height, border, align) {
						//获取按钮同级元素，就是input标签
						var input = _self.siblings("input");
						//把上传成功图片的url放到input里
						input.val(url);
						//清理同级的img标签
						input.parent().find("img").remove();
						//在input后面追加一个img标签，该img标签用a标签包裹，其实就是在回显图片
						input.after("<a href='"+url+"' target='_blank'><img src='"+url+"' width='80' height='50'/></a>");
						//关闭上传界面
						this.hideDialog();
					}
				});
			});
		});
    }
};
