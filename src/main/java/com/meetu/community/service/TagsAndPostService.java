package com.meetu.community.service;

import java.util.List;



public interface TagsAndPostService {
	
	public void insertPost(Integer postId,Integer tagsId);

	public void deleteTagsAndPostByPostId(Integer postId);

	public List<Integer> selectPostIdsByTagId(Integer tagId);
	
}
