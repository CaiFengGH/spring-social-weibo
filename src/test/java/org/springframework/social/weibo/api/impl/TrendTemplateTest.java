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

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.header;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

import java.util.List;

import org.junit.Test;
import org.springframework.social.weibo.api.UserTrend;

public class TrendTemplateTest extends AbstractWeiboOperationsTest {

	private TrendTemplate trendTemplate;

	@Test
	public void testGetTrendsPagination() {
		mockServer
				.expect(requestTo("https://api.weibo.com/2/trends.json?uid=1&count=20&page=2"))
				.andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth2 accessToken"))
				.andRespond(
						withResponse(jsonResource("userTrends"),
								responseHeaders));
		List<UserTrend> trends = trendTemplate.getTrends(1, 20, 2);
		assertEquals(2, trends.size());
		UserTrend firstTrend = trends.iterator().next();
		verifyUserTrend(firstTrend);
	}

	@Test
	public void testGetTrends() {
		mockServer
				.expect(requestTo("https://api.weibo.com/2/trends.json?uid=1"))
				.andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth2 accessToken"))
				.andRespond(
						withResponse(jsonResource("userTrends"),
								responseHeaders));
		List<UserTrend> trends = trendTemplate.getTrends(1);
		assertEquals(2, trends.size());
		UserTrend firstTrend = trends.iterator().next();
		verifyUserTrend(firstTrend);
	}

	private void verifyUserTrend(UserTrend userTrend) {
		assertEquals(1567898, userTrend.getId());
		assertEquals("苹果", userTrend.getHotword());
		assertEquals("225673", userTrend.getNum());
	}

	@Override
	public void setUp() {
		trendTemplate = new TrendTemplate(getObjectMapper(), getRestTemplate(),
				true);
	}

}
