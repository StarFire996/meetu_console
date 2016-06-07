package com.meetu.community.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.meetu.community.domain.Activity;
import com.meetu.community.domain.Tags;

public interface ActivityService {

	public List<Activity> selectActivityByType(Integer type);

	public void insertActivity(Activity activity);
	
	public void parseActivityListToJson(List<Activity> actList,JSONArray actArray);

	public PageInfo<Activity> selectActivityList(Integer page, Integer rows);

	public void updateActivity(Activity activity);

	public void deleteActivity(int parseInt);
}
