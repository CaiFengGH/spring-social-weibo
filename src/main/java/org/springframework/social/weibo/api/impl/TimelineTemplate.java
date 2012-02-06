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
import org.springframework.social.weibo.api.Status;
import org.springframework.social.weibo.api.StatusContentType;
import org.springframework.social.weibo.api.TimelineOperations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class TimelineTemplate extends AbstractWeiboOperations implements
		TimelineOperations {

	protected TimelineTemplate(ObjectMapper objectMapper,
			RestTemplate restTemplate, boolean isAuthorized) {
		super(objectMapper, restTemplate, isAuthorized);
	}

	@Override
	public CursoredList<Status> getFriendsTimeline() {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				buildUri("statuses/friends_timeline.json"), JsonNode.class);
		return deserializeCursoredList(dataNode, Status.class, "statuses");
	}

	@Override
	public CursoredList<Status> getFriendsTimeline(int pageSize, int pageNumber) {
		return getFriendsTimeline(pageSize, pageNumber, false);
	}

	@Override
	public CursoredList<Status> getFriendsTimeline(int pageSize,
			int pageNumber, boolean onlyApplicationStatus) {
		return getFriendsTimeline(0, 0, pageSize, pageNumber,
				onlyApplicationStatus, StatusContentType.ALL);
	}

	@Override
	public CursoredList<Status> getFriendsTimeline(long sinceId, long maxId,
			int pageSize, int pageNumber, boolean onlyApplicationStatus,
			StatusContentType statusContentType) {
		return fetchStatusList("statuses/friends_timeline.json", sinceId,
				maxId, pageSize, pageNumber, onlyApplicationStatus,
				statusContentType);
	}

	private CursoredList<Status> fetchStatusList(String url, long sinceId,
			long maxId, int pageSize, int pageNumber,
			boolean onlyApplicationStatus, StatusContentType statusContentType) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				uriBuilder(url)
						.queryParam("since_id", String.valueOf(sinceId))
						.queryParam("max_id", String.valueOf(maxId))
						.queryParam("count", String.valueOf(pageSize))
						.queryParam("page", String.valueOf(pageNumber))
						.queryParam("base_app",
								formatBaseAppValue(onlyApplicationStatus))
						.queryParam("feature",
								String.valueOf(statusContentType.ordinal()))
						.build(), JsonNode.class);
		return deserializeCursoredList(dataNode, Status.class, "statuses");
	}

	private String formatBaseAppValue(boolean onlyApplicationStatus) {
		return onlyApplicationStatus ? "1" : "0";
	}

	@Override
	public CursoredList<Status> getHomeTimeline() {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				buildUri("statuses/home_timeline.json"), JsonNode.class);
		return deserializeCursoredList(dataNode, Status.class, "statuses");
	}

	@Override
	public CursoredList<Status> getHomeTimeline(int pageSize, int pageNumber) {
		return getHomeTimeline(pageSize, pageNumber, false);
	}

	@Override
	public CursoredList<Status> getHomeTimeline(int pageSize, int pageNumber,
			boolean onlyApplicationStatus) {
		return getHomeTimeline(0, 0, pageSize, pageNumber,
				onlyApplicationStatus, StatusContentType.ALL);
	}

	@Override
	public CursoredList<Status> getHomeTimeline(long sinceId, long maxId,
			int pageSize, int pageNumber, boolean onlyApplicationStatus,
			StatusContentType statusContentType) {
		return fetchStatusList("statuses/home_timeline.json", sinceId, maxId,
				pageSize, pageNumber, onlyApplicationStatus, statusContentType);
	}

	@Override
	public CursoredList<Status> getPublicTimeline() {
		JsonNode dataNode = restTemplate.getForObject(
				buildUri("statuses/public_timeline.json"), JsonNode.class);
		return deserializeCursoredList(dataNode, Status.class, "statuses");
	}

	@Override
	public CursoredList<Status> getPublicTimeline(int pageSize, int pageNumber) {
		return getPublicTimeline(pageSize, pageNumber, false);
	}

	@Override
	public CursoredList<Status> getPublicTimeline(int pageSize, int pageNumber,
			boolean onlyApplicationStatus) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				uriBuilder("statuses/public_timeline.json")
						.queryParam("count", String.valueOf(pageSize))
						.queryParam("page", String.valueOf(pageNumber))
						.queryParam("base_app",
								formatBaseAppValue(onlyApplicationStatus))
						.build(), JsonNode.class);
		return deserializeCursoredList(dataNode, Status.class, "statuses");
	}

	@Override
	public CursoredList<Status> getUserTimeline(long uid) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				buildUri("statuses/user_timeline.json", "uid", uid),
				JsonNode.class);
		return deserializeCursoredList(dataNode, Status.class, "statuses");
	}

	@Override
	public CursoredList<Status> getUserTimeline(long uid, int pageSize,
			int pageNumber) {
		return getUserTimeline(uid, pageSize, pageNumber, false);
	}

	@Override
	public CursoredList<Status> getUserTimeline(long uid, int pageSize,
			int pageNumber, boolean onlyApplicationStatus) {
		return getUserTimeline(uid, 0, 0, pageSize, pageNumber,
				onlyApplicationStatus, StatusContentType.ALL);
	}

	@Override
	public CursoredList<Status> getUserTimeline(long uid, long sinceId,
			long maxId, int pageSize, int pageNumber,
			boolean onlyApplicationStatus, StatusContentType statusContentType) {
		requireAuthorization();
		JsonNode dataNode = restTemplate.getForObject(
				uriBuilder("statuses/user_timeline.json")
						.queryParam("uid", String.valueOf(uid))
						.queryParam("since_id", String.valueOf(sinceId))
						.queryParam("max_id", String.valueOf(maxId))
						.queryParam("count", String.valueOf(pageSize))
						.queryParam("page", String.valueOf(pageNumber))
						.queryParam("base_app",
								formatBaseAppValue(onlyApplicationStatus))
						.queryParam("feature",
								String.valueOf(statusContentType.ordinal()))
						.build(), JsonNode.class);
		return deserializeCursoredList(dataNode, Status.class, "statuses");
	}

	@Override
	public Status updateStatus(String message) {
		requireAuthorization();
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>(
				1);
		request.add("status", message);
		return restTemplate.postForObject(buildUri("statuses/update.json"),
				request, Status.class);
	}

	@Override
	public Status updateStatus(String message, Float latitude, Float longitude) {
		requireAuthorization();
		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>(
				1);
		request.add("status", message);
		request.add("lat", latitude.toString());
		request.add("long", longitude.toString());
		return restTemplate.postForObject(buildUri("statuses/update.json"),
				request, Status.class);
	}

}
