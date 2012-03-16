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
import org.springframework.social.weibo.api.CursoredList;
import org.springframework.social.weibo.api.Favorite;
import org.springframework.social.weibo.api.Favorite.Tag;
import org.springframework.social.weibo.matcher.StatusMatcher;

public class FavoriteTemplateTest extends AbstractWeiboOperationsTest {

	private FavoriteTemplate favoriteTemplate;

	@Override
	public void setUp() {
		favoriteTemplate = new FavoriteTemplate(getObjectMapper(),
				getRestTemplate(), true);
	}

	@Test
	public void testGetFavorite() {
		mockServer
				.expect(requestTo("https://api.weibo.com/2/favorites/show.json?id=1"))
				.andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth2 accessToken"))
				.andRespond(
						withResponse(jsonResource("favorite"), responseHeaders));
		verifyFavorite(favoriteTemplate.getFavorite(1));
	}

	@Test
	public void testGetFavorites() {
		mockServer
				.expect(requestTo("https://api.weibo.com/2/favorites.json"))
				.andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth2 accessToken"))
				.andRespond(
						withResponse(jsonResource("cursoredFavorites"),
								responseHeaders));
		CursoredList<Favorite> cursoredList = favoriteTemplate.getFavorites();
		assertEquals(16, cursoredList.getTotalNumber());
		assertEquals(2, cursoredList.size());
		Favorite firstFavorite = cursoredList.iterator().next();
		verifyFavorite(firstFavorite);
	}

	private void verifyFavorite(Favorite favorite) {
		List<Tag> tags = favorite.getTags();
		assertEquals(2, tags.size());
		Tag firstTag = tags.iterator().next();
		verifyTag(firstTag);
		assertEquals(1306998976000L, favorite.getFavoritedTime().getTime());
		StatusMatcher.verifyStatus(favorite.getStatus());
	}

	@Test
	public void testGetFavoritesIntInt() {
		mockServer
				.expect(requestTo("https://api.weibo.com/2/favorites.json?count=20&page=5"))
				.andExpect(method(GET))
				.andExpect(header("Authorization", "OAuth2 accessToken"))
				.andRespond(
						withResponse(jsonResource("cursoredFavorites"),
								responseHeaders));
		CursoredList<Favorite> cursoredList = favoriteTemplate.getFavorites(20,
				5);
		assertEquals(16, cursoredList.getTotalNumber());
		assertEquals(2, cursoredList.size());
		Favorite firstFavorite = cursoredList.iterator().next();
		verifyFavorite(firstFavorite);
	}

	private void verifyTag(Tag firstTag) {
		assertEquals(23, firstTag.getId());
		assertEquals("80后", firstTag.getValue());
		assertEquals(25369, firstTag.getCount());
	}

}
