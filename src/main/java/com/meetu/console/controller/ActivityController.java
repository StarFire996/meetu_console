package com.meetu.console.controller;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.meetu.community.domain.Activity;
import com.meetu.community.domain.Tags;
import com.meetu.community.service.ActivityService;
import com.meetu.community.service.TagsService;
import com.meetu.console.domain.EasyUIResult;

@Controller
@RequestMapping("console/activity")
public class ActivityController {
	
	@Autowired
	private TagsService tagsService;
	
	@Autowired
	private ActivityService activityService;
	
	public static Logger LOGGER = LoggerFactory.getLogger(PostController.class);
	
	@RequestMapping("getActivity")
	public ResponseEntity<EasyUIResult> getActivity(@RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows){
		try {
			PageInfo<Activity> pageInfo = this.activityService.selectActivityList(page,rows);
			
			EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
			
			return ResponseEntity.ok(easyUIResult);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("getActivity_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	@RequestMapping("updateActivity")
	public ResponseEntity<Void> updateActivity(Activity activity){
		try {
			this.activityService.updateActivity(activity);
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("updateActivity_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	@RequestMapping("addActivity")
	public ResponseEntity<Void> addActivity(Activity activity){
		try {
			if (activity != null) {
				activity.setCreateAt(new Timestamp(System.currentTimeMillis()));
				this.activityService.insertActivity(activity);
			}
			return ResponseEntity.ok().body(null);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("addActivity_err:{}",e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
	
	@RequestMapping(value="deleteActivity",method=RequestMethod.POST)
	public ResponseEntity<Integer> deleteTags(@RequestParam("ids")String ids){
		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] split = ids.split(",");
				for (String string : split) {
					this.activityService.deleteActivity(Integer.parseInt(string));
				}
			}
			return ResponseEntity.ok().body(200);
		} catch (Exception e) {
			e.printStackTrace();
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("deleteActivity_err:"+ids+""+e.getMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	}
}
