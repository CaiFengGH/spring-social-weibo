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

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.social.weibo.api.CursoredList;
import org.springframework.social.weibo.api.WeiboProfile;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.social.weibo.api.impl.WeiboProfileMatcher.verifyWeiboProfile;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class FriendTemplateTest extends AbstractWeiboOperationsTest {

    private FriendTemplate friendTemplate;

    @Override
    public void setUp() {
        friendTemplate = new FriendTemplate(getObjectMapper(),
                getRestTemplate(), true);
    }

    @Test
    public void testCreateFriend() {
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/create.json"))
                .andExpect(method(POST))
                .andExpect(content().string("uid=1"))
                .andExpect(header("Authorization", "OAuth2 accessToken"))
                .andRespond(
                        withSuccess(jsonResource("profile"), MediaType.APPLICATION_JSON));

        WeiboProfile user = friendTemplate.createFriend(1L);
        verifyWeiboProfile(user);
    }

    @Test
    public void testGetActiveFollowers() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/followers/active.json?uid="
                        + uid))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("users"), MediaType.APPLICATION_JSON));
        List<WeiboProfile> users = friendTemplate.getActiveFollowers(uid);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetActiveFollowersPagination() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/followers/active.json?uid=123&count=20"))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("users"), MediaType.APPLICATION_JSON));
        List<WeiboProfile> users = friendTemplate.getActiveFollowers(uid, 20);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetBilateralFriends() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/friends/bilateral.json?uid="
                        + uid))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> users = friendTemplate
                .getBilateralFriends(uid);
        assertEquals(2, users.size());
        assertEquals(650, users.getTotalNumber());
        assertEquals(0, users.getPreviousCursor());
        assertEquals(1, users.getNextCursor());
    }

    @Test
    public void testGetBilateralFriendsPagination() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/friends/bilateral.json?uid=123&count=20&cursor=5"))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> users = friendTemplate.getBilateralFriends(
                uid, 20, 5);
        assertEquals(2, users.size());
        assertEquals(650, users.getTotalNumber());
        assertEquals(0, users.getPreviousCursor());
        assertEquals(1, users.getNextCursor());
    }

    @Test
    public void testGetCommonFriends() {
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/friends/in_common.json?uid=123&suid=456"))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> users = friendTemplate.getCommonFriends(
                123L, 456L);
        assertEquals(2, users.size());
        assertEquals(650, users.getTotalNumber());
        assertEquals(0, users.getPreviousCursor());
        assertEquals(1, users.getNextCursor());
    }

    @Test
    public void testGetCommonFriendsPagination() {
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/friends/in_common.json?uid=123&suid=456&count=20&page=5"))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> users = friendTemplate.getCommonFriends(
                123L, 456L, 20, 5);
        assertEquals(2, users.size());
        assertEquals(650, users.getTotalNumber());
        assertEquals(0, users.getPreviousCursor());
        assertEquals(1, users.getNextCursor());
    }

    @Test
    public void testGetFollowers() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/followers.json?uid="
                        + uid))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> users = friendTemplate.getFollowers(uid);
        assertEquals(2, users.size());
        assertEquals(650, users.getTotalNumber());
        assertEquals(0, users.getPreviousCursor());
        assertEquals(1, users.getNextCursor());
    }

    @Test
    public void testGetFollowersPagination() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/followers.json?uid=123&count=20&cursor=5"))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> users = friendTemplate.getFollowers(uid, 20,
                5);
        assertEquals(2, users.size());
        assertEquals(650, users.getTotalNumber());
        assertEquals(0, users.getPreviousCursor());
        assertEquals(1, users.getNextCursor());
    }

    @Test
    public void testGetFriends() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/friends.json?uid="
                        + uid))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> friends = friendTemplate.getFriends(uid);
        assertEquals(2, friends.size());
        assertEquals(650, friends.getTotalNumber());
        assertEquals(0, friends.getPreviousCursor());
        assertEquals(1, friends.getNextCursor());
    }

    @Test
    public void testGetFriendsPagination() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/friends.json?uid=123&count=20&cursor=5"))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("cursoredUsers"),
                                MediaType.APPLICATION_JSON));
        CursoredList<WeiboProfile> friends = friendTemplate.getFriends(uid, 20,
                5);
        assertEquals(2, friends.size());
        assertEquals(650, friends.getTotalNumber());
        assertEquals(0, friends.getPreviousCursor());
        assertEquals(1, friends.getNextCursor());
    }

    @Test
    public void testDeleteFriend() {
        mockServer
                .expect(requestTo("https://api.weibo.com/2/friendships/destroy.json"))
                .andExpect(method(POST))
                .andExpect(content().string("uid=1"))
                .andExpect(header("Authorization", "OAuth2 accessToken"))
                .andRespond(
                        withSuccess(jsonResource("profile"), MediaType.APPLICATION_JSON));

        WeiboProfile user = friendTemplate.deleteFriend(1L);
        verifyWeiboProfile(user);
    }

}
