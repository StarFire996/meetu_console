package com.meetu.community.service;

import java.sql.Timestamp;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.meetu.community.domain.Post;
import com.meetu.console.domain.PostResult;

public interface PostService {

	public void updatePost(Post post);
	
	public Post selectPostById(Integer id);

	public void savePost(Post post, String tags, String imgs);
	
	public List<Post> selectPostListByTagId(Timestamp createAt,Integer tagId);

	public void parsePostListToJson(Integer userCodeMine,List<Post> list, JSONArray postArray) throws Exception ;

	public List<Post> selectRecommendPostList(Timestamp timestamp);

	public List<Post> selectNewPostList(Timestamp timestamp);

	public List<Post> selectFriendPostList(String userFromId,Timestamp timestamp);

	public List<Post> selectPostListByUserCode(Integer userFrom,Timestamp timestamp);

	public void deletePost(Integer postId);

	public void parsePostToJson(Integer userFrom, Post post, JSONObject json2)throws Exception;

	public List<PostResult> selectPostList(Integer page, Integer rows);

	public Long selectCount();

}
