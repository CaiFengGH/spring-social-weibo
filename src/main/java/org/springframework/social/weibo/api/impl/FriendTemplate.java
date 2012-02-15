/*
 * Copyright 2011 France Telecom R&D Beijing Co., Ltd 北京法国电信研发中心有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.social.weibo.api.impl;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.weibo.api.CursoredList;
import org.springframework.social.weibo.api.FriendOperations;
import org.springframework.social.weibo.api.WeiboProfile;
import org.springframework.web.client.RestTemplate;

class FriendTemplate extends AbstractWeiboOperations implements
		FriendOperations {

	protected FriendTemplate(ObjectMapper objectMapper,
			RestTemplate restTemplate, boolean isAuthorized) {
		super(objectMapper, restTemplate, isAuthorized);
	}

	private CursoredList<WeiboProfile> fetchUsersList(String url, long uid,
			int pageSize, int pageNumber) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				uriBuilder(url).queryParam("uid", String.valueOf(uid))
						.queryParam("count", String.valueOf(pageSize))
						.queryParam("cursor", String.valueOf(pageNumber))
						.build(), JsonNode.class);
		return deserializeCursoredList(dataNode, WeiboProfile.class, "users");
	}

	@Override
	public CursoredList<WeiboProfile> getBilateralFriends(long uid) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				buildUri("friendships/friends/bilateral.json", "uid", uid),
				JsonNode.class);
		return deserializeCursoredList(dataNode, WeiboProfile.class, "users");
	}

	@Override
	public CursoredList<WeiboProfile> getBilateralFriends(long uid,
			int pageSize, int pageNumber) {
		return fetchUsersList("friendships/friends/bilateral.json", uid,
				pageSize, pageNumber);
	}

	@Override
	public CursoredList<WeiboProfile> getFollowers(long uid) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				buildUri("friendships/followers.json", "uid", uid),
				JsonNode.class);
		return deserializeCursoredList(dataNode, WeiboProfile.class, "users");
	}

	@Override
	public CursoredList<WeiboProfile> getFollowers(long uid, int pageSize,
			int pageNumber) {
		return fetchUsersList("friendships/followers.json", uid, pageSize,
				pageNumber);
	}

	@Override
	public CursoredList<WeiboProfile> getFriends(long uid) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				buildUri("friendships/friends.json", "uid", uid),
				JsonNode.class);
		return deserializeCursoredList(dataNode, WeiboProfile.class, "users");
	}

	@Override
	public CursoredList<WeiboProfile> getFriends(long uid, int pageSize,
			int pageNumber) {
		return fetchUsersList("friendships/friends.json", uid, pageSize,
				pageNumber);
	}
}
