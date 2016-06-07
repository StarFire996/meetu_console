package com.meetu.community.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.meetu.community.domain.Image;
import com.meetu.community.domain.Post;
import com.meetu.community.domain.Praise;
import com.meetu.community.domain.Tags;
import com.meetu.community.domain.User;
import com.meetu.community.mapper.PostMapper;
import com.meetu.community.service.CommentService;
import com.meetu.community.service.ImageService;
import com.meetu.community.service.MeetuFriendsRelService;
import com.meetu.community.service.NotifyService;
import com.meetu.community.service.PostService;
import com.meetu.community.service.PraiseService;
import com.meetu.community.service.TagsAndPostService;
import com.meetu.community.service.TagsService;
import com.meetu.community.service.UserService;
import com.meetu.console.domain.PostResult;
import com.meetu.util.StsService;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	private PostMapper postMapper;

	@Autowired
	private TagsService tagsService;

	@Autowired
	private UserService userService;

	@Autowired
	private TagsAndPostService tagsAndPostService;

	@Autowired
	private PraiseService praiseService;

	@Autowired
	private MeetuFriendsRelService friendsRelService;

	@Autowired
	private NotifyService notifyService;

	@Autowired
	private CommentService commentService;
	
	@Autowired
	private ImageService imageService;

	public void updatePost(Post post) {
		this.postMapper.updatePost(post);
	}

	public Post selectPostById(Integer id) {
		return this.postMapper.selectPostById(id);
	}

	public void savePost(Post post, String tags, String imgs) {
		//解析图片数据,向图片库插入数据,将id以,拼接
		StringBuilder sb = new StringBuilder("");
		if (StringUtils.isNotBlank(imgs)) {
			JSONArray parseArray = JSONObject.parseArray(imgs);
			for (int i = 0; i < parseArray.size(); i++) {
				JSONObject img = (JSONObject) parseArray.get(i);
				Image image = new Image();
				image.setDesc(img.getString("desc"));
				image.setExten(img.getString("exten"));
				image.setHight(img.getInteger("h"));
				image.setWeight(img.getInteger("w"));
				image.setUrl(img.getString("url"));
				imageService.insertImage(image);
				sb.append(image.getId()).append(",");
			}
			post.setImgs(sb.toString());
		}
		this.postMapper.insertPost(post);
		//解析tag数组
		if (StringUtils.isNotBlank(tags)) {
			List<Integer> tagIds = JSONObject.parseArray(tags, Integer.class);
			for (Integer tagId : tagIds) {
				// 帖子对应标签的帖子计数加1
				Tags tag = this.tagsService.selectTagsById(tagId);
				tag.setPostNum(tag.getPostNum() + 1);
				this.tagsService.updateTag(tag);
				// 向中间表插一条数据
				this.tagsAndPostService.insertPost(post.getId(), tagId);
			}
		}
	}

	// 根据帖子id查询帖子
	public List<Post> selectPostListByTagId(Timestamp createAt, Integer tagId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tagId", tagId);
		map.put("createAt", createAt);
		return this.postMapper.selectPostListByTagId(map);
	}

	// 生成json串
	public void parsePostListToJson(Integer userFrom, List<Post> list,
			JSONArray postArray) throws Exception {
		for (Post post : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("postId", post.getId());
			Integer userCode = post.getCreateBy();
			User user = this.userService.selectUserByCode(userCode);
			String userName = user.getNickname();
			String userHead = StsService.generateCircleUrl(user.getIcon_url());
			String userSchool = user.getSchool();
			String userSex = user.getSex();
			jsonObject.put("userSex", userSex);
			jsonObject.put("userCode", userCode);
			jsonObject.put("userName", userName);
			jsonObject.put("userHead", userHead);
			jsonObject.put("userSchool", userSchool);
			jsonObject.put("createAt", post.getCreateAt().getTime() / 1000);
			
			JSONArray imgArray = new JSONArray();
			this.imageService.parseImgToJson(imgArray,post.getImgs());
			
			jsonObject.put("imgs", imgArray);
			jsonObject.put("content", post.getContent());
			jsonObject.put("praiseNum", post.getPraiseNum());
			jsonObject.put("commentNum", post.getCommentNum());
			Praise praise = new Praise();
			praise.setUserFrom(userFrom);
			praise.setPostId(post.getId());
			Integer isPraise = this.praiseService.isPraise(praise);
			jsonObject.put("isPraise", isPraise);

			List<Tags> tagList = this.tagsService.selectTagsListByPostId(post
					.getId());
			JSONArray tags = new JSONArray();
			this.tagsService.parseTagsListToJson(tagList, tags);
			jsonObject.put("tags", tags);
			postArray.add(jsonObject);
		}
	}

	@Override
	public void parsePostToJson(Integer userFrom, Post post, JSONObject json2)
			throws Exception {
		json2.put("postId", post.getId());
		Integer userCode = post.getCreateBy();
		User user = this.userService.selectUserByCode(userCode);
		String userName = user.getNickname();
		String userHead = StsService.generateCircleUrl(user.getIcon_url());
		String userSchool = user.getSchool();
		String userSex = user.getSex();
		json2.put("userCode", userCode);
		json2.put("userName", userName);
		json2.put("userHead", userHead);
		json2.put("userSchool", userSchool);
		json2.put("userSex", userSex);
		json2.put("createAt", post.getCreateAt().getTime() / 1000);
		
		JSONArray imgArray = new JSONArray();
		this.imageService.parseImgToJson(imgArray,post.getImgs());
		json2.put("imgs", imgArray);
		
		json2.put("content", post.getContent());
		json2.put("praiseNum", post.getPraiseNum());
		json2.put("commentNum", post.getCommentNum());
		Praise praise = new Praise();
		praise.setUserFrom(userFrom);
		praise.setPostId(post.getId());
		Integer isPraise = this.praiseService.isPraise(praise);
		json2.put("isPraise", isPraise);

		List<Tags> tagList = this.tagsService.selectTagsListByPostId(post
				.getId());
		JSONArray tags = new JSONArray();
		this.tagsService.parseTagsListToJson(tagList, tags);
		json2.put("tags", tags);
	}

	public List<Post> selectRecommendPostList(Timestamp timestamp) {
		return this.postMapper.selectRecommendPostList(timestamp);
	}

	public List<Post> selectNewPostList(Timestamp timestamp) {
		return this.postMapper.selectNewPostList(timestamp);
	}

	public List<Post> selectFriendPostList(String userFromId,
			Timestamp timestamp) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userFromId", userFromId);
		map.put("timestamp", timestamp);

		return this.postMapper.selectFriendPostListById(map);
	}

	public List<Post> selectPostListByUserCode(Integer userFrom,
			Timestamp timestamp) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userFrom", userFrom);
		map.put("timestamp", timestamp);
		return this.postMapper.selectPostListByCode(map);
	}

	// 删除帖子
	public void deletePost(Integer postId) {
		// 删除相关通知
		this.notifyService.deleteNotifyByPostId(postId);
		// 删除相关赞
		this.praiseService.deletePraiseByPostId(postId);
		// 删除相关评论
		this.commentService.deleteCommentByPostId(postId);
		// 删除帖子/标签中间表数据
		this.tagsAndPostService.deleteTagsAndPostByPostId(postId);
		// 删除帖子中的图片数据
		Post post = this.postMapper.selectPostById(postId);
		String imgs = post.getImgs();
		this.imageService.deleteImage(imgs);
		// 删除帖子
		this.postMapper.deletePostById(postId);
	}

	public List<PostResult> selectPostList(Integer page, Integer rows) {
		PageHelper.startPage(page, rows);
		List<Post> list = this.postMapper.selectPostList();
		List<PostResult> pList = new ArrayList<PostResult>();
		if (list != null && list.size()>0) {
			for (Post post : list) {
				PostResult postResult = new PostResult();
				postResult.setCommentNum(post.getCommentNum());
				postResult.setContent(post.getContent());
				postResult.setCreateAt(post.getCreateAt());
				postResult.setCreateBy(post.getCreateBy());
				postResult.setId(post.getId());
				postResult.setLevel(post.getLevel());
				postResult.setPraiseNum(post.getPraiseNum());
				postResult.setType(post.getType());
				
				JSONArray imgArray = new JSONArray();
				this.imageService.parseImgToJson(imgArray,post.getImgs());
				if (imgArray.size()>0) {
					switch (imgArray.size()) {
					case 9:
						postResult.setPic9(((JSONObject)imgArray.get(8)).getString("url"));
					case 8:
						postResult.setPic8(((JSONObject)imgArray.get(7)).getString("url"));
					case 7:
						postResult.setPic7(((JSONObject)imgArray.get(6)).getString("url"));
					case 6:
						postResult.setPic6(((JSONObject)imgArray.get(5)).getString("url"));
					case 5:
						postResult.setPic5(((JSONObject)imgArray.get(4)).getString("url"));
					case 4:
						postResult.setPic4(((JSONObject)imgArray.get(3)).getString("url"));
					case 3:
						postResult.setPic3(((JSONObject)imgArray.get(2)).getString("url"));
					case 2:
						postResult.setPic2(((JSONObject)imgArray.get(1)).getString("url"));
					case 1:
						postResult.setPic1(((JSONObject)imgArray.get(0)).getString("url"));

					default:
						break;
					}
				}
				pList.add(postResult);
			}
			
		}
		return pList;
	}

	public Long selectCount() {
		return this.postMapper.selectCount();
	}


}
