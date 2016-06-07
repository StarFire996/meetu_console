package com.meetu.console.controller;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.meetu.community.domain.Post;
import com.meetu.community.domain.Tags;
import com.meetu.community.service.TagsService;
import com.meetu.console.domain.EasyUIResult;

@Controller
@RequestMapping("console/tag")
public class TagController {
	
	@Autowired
	private TagsService tagsService;
	
	public static Logger LOGGER = LoggerFactory.getLogger(PostController.class);
	
	@RequestMapping("getTag")
	public ResponseEntity<EasyUIResult> getTag(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows){
		try {
			PageInfo<Tags> pageInfo = this.tagsService.selectTagList(page,rows);
			
			EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
			
			return ResponseEntity.ok(easyUIResult);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("getTag_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	@RequestMapping("updateTag")
	public ResponseEntity<Void> updateTag(Tags tag){
		try {
			this.tagsService.updateTag(tag);
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("updateTag_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	@RequestMapping("addTag")
	public ResponseEntity<Void> addTag(Tags tags){
		try {
			if (tags != null) {
				tags.setCreateAt(new Timestamp(System.currentTimeMillis()));
				tags.setPostNum(0);
				String content = tags.getContent();
				if (StringUtils.isNotBlank(content)) {
					tags.setLength(content.length());
				}else{
					tags.setLength(0);
				}
				this.tagsService.insertTag(tags);
			}
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("updateTag_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="deleteTags",method=RequestMethod.POST)
	public ResponseEntity<Integer> deleteTags(@RequestParam("ids")String ids){
		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] split = ids.split(",");
				for (String string : split) {
					this.tagsService.deleteTag(Integer.parseInt(string));
				}
			}
			return ResponseEntity.ok().body(200);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("deletePost_err:"+ids+""+e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
