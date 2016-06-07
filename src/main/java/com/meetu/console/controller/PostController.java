package com.meetu.console.controller;

import java.util.List;

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

import com.meetu.community.domain.Post;
import com.meetu.community.service.PostService;
import com.meetu.console.domain.EasyUIResult;
import com.meetu.console.domain.PostResult;

@Controller
@RequestMapping("console/post")
public class PostController {

	@Autowired
	private PostService postService;
	
	public static Logger LOGGER = LoggerFactory.getLogger(PostController.class);

	@RequestMapping("getImg")
	public ResponseEntity<EasyUIResult> getImg(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows){
		try {
			List<PostResult> pageInfo = this.postService.selectPostList(page,rows);
			
			Long total = this.postService.selectCount();
			
			EasyUIResult easyUIResult = new EasyUIResult(total, pageInfo);
			
			return ResponseEntity.ok(easyUIResult);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("getImg_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="getPost/{id}",method=RequestMethod.GET)
	public ResponseEntity<Post> getPost(@PathVariable("id") Integer id){
		try {
			Post post = this.postService.selectPostById(id);
			return ResponseEntity.ok(post);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("getPost_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping("updatePost")
	public ResponseEntity<Void> updatePost(Post post){
		try {
			this.postService.updatePost(post);
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("updatePost_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="deletePost",method=RequestMethod.POST)
	public ResponseEntity<Integer> deletePost(@RequestParam("ids")String ids){
		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] split = ids.split(",");
				for (String string : split) {
					this.postService.deletePost(Integer.parseInt(string));
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
