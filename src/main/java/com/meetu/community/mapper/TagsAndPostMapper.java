package com.meetu.community.mapper;

import java.util.HashMap;
import java.util.List;

public interface TagsAndPostMapper {

	public void insertObject(HashMap<String, Object> map);
	
	public void deleteTagsAndPostByPostId(Integer postId);

	public List<Integer> selectPostIdsByTagId(Integer tagId);

}
