package com.meetu.community.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.meetu.community.domain.Tags;
import com.meetu.console.domain.PostResult;

public interface TagsService {

	public Tags selectTagsById(Integer id);

	public void insertTag(Tags tags);

	public void updateTag(Tags tags);

	public List<Tags> selectTagsListByName(String searchStr);

	public List<Tags> selectTagsListByRec();

	public List<Tags> selectTagsListByHot();

	public List<Tags> selectTagsListByNew(Tags tags);
	
	public void parseTagsListToJson(List<Tags> list,JSONArray jsonArray);

	public List<Tags> selectTagsListByPostId(Integer id);

	public PageInfo<Tags> selectTagList(Integer page, Integer rows);

	public void deleteTag(Integer id);

}
