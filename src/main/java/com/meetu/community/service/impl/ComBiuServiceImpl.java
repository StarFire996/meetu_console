package com.meetu.community.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.meetu.community.domain.ComBiu;
import com.meetu.community.domain.MeetuFriendsRel;
import com.meetu.community.domain.User;
import com.meetu.community.mapper.ComBiuMapper;
import com.meetu.community.service.ComBiuService;
import com.meetu.community.service.MeetuFriendsRelService;
import com.meetu.community.service.UserService;
import com.meetu.util.Common;
import com.meetu.util.StsService;

@Service
@Transactional
public class ComBiuServiceImpl implements ComBiuService {

	@Autowired
	private ComBiuMapper biuMapper;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MeetuFriendsRelService relService;

	public void insertBiu(ComBiu biu) {
		this.biuMapper.insertBiu(biu);
	}

	public void setBiuRead(Integer userCodeMine) {
		this.biuMapper.setBiuRead(userCodeMine);
	}

	public Integer selectBiuUnRead(Integer userCodeMine) {
		return this.biuMapper.selectBiuNumUnRead(userCodeMine);
	}

	// 查询以我的code为被抢code的biu
	public List<ComBiu> selectMyBiuList(Integer userFrom, Timestamp timestamp) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("userCodeMine", userFrom);
		map.put("timestamp", timestamp);
		List<ComBiu> list = this.biuMapper.selectBiuByUserCodeMine(map);
		return list;
	}

	public void parseBiuToJson(List<ComBiu> biuList, JSONArray biuArr)
			throws Exception {
		for (ComBiu biu : biuList) {
			JSONObject jsonObject = new JSONObject();
			Integer userFrom = biu.getUserCodeGrab();
			User user = this.userService.selectUserByCode(userFrom);
			String userName = user.getNickname();
			String userHead = StsService.generateCircleUrl(user.getIcon_url());
			String userSchool = user.getSchool();
			String sex = user.getSex();
			String starsign = user.getStarsign();
			int age = Common.getAge(user.getBirth_date());
			Integer isAccept = biu.getStatus();
			jsonObject.put("userCode", userFrom);
			jsonObject.put("userName", userName);
			jsonObject.put("userHead", userHead);
			jsonObject.put("userSchool", userSchool);
			jsonObject.put("sex", sex);
			jsonObject.put("starsign", starsign);
			jsonObject.put("age", age);
			jsonObject.put("isAccept", isAccept);
			jsonObject.put("createAt", biu.getCreateAt().getTime() / 1000);
			biuArr.add(jsonObject);
		}
	}

	// 删除以我的code为被抢code的biu
	public void deleteBiuByUserCode(Integer userFrom) {
		this.biuMapper.deleteBiuByUserCode(userFrom);
	}


	// 双方加好友
	public void addFriends(String id1, String id2) throws Exception {
		if (id1 == id2 || isFriends(id1, id2)) {

		} else {
			MeetuFriendsRel friendsRel = new MeetuFriendsRel();
			friendsRel.setId(Common.generateId());
			friendsRel.setDate(new Date());
			friendsRel.setUser_id1(id1);
			friendsRel.setUser_id2(id2);
			relService.insertOper(friendsRel);
		}
	}

	// 判断双方是否是好友关系
	public boolean isFriends(String id1, String id2) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id1", id1);
		map.put("user_id2", id2);
		if (relService.isFriends(map) != null) {
			return true;
		} else {
			return false;
		}
	}

}
