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

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.weibo.api.impl.WeiboProfileMatcher.verifyWeiboProfile;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.social.weibo.api.WeiboProfile;

public class UserTemplateTest extends AbstractWeiboOperationsTest {

    private UserTemplate userTemplate;

    @Override
    public void setUp() {
        userTemplate = new UserTemplate(getObjectMapper(), getRestTemplate(),
                true);
    }

    @Test
    public void testGetUserProfileByDomainName() {
        mockServer
                .expect(requestTo("https://api.weibo.com/2/users/domain_show.json?domain=domain"))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("profile"), MediaType.APPLICATION_JSON));

        WeiboProfile profile = userTemplate
                .getUserProfileByDomainName("domain");
        verifyWeiboProfile(profile);
    }

    @Test
    public void testGetUserProfileById() {
        long uid = 123L;
        mockServer
                .expect(requestTo("https://api.weibo.com/2/users/show.json?uid="
                        + uid))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("profile"), MediaType.APPLICATION_JSON));

        WeiboProfile profile = userTemplate.getUserProfileById(uid);
        verifyWeiboProfile(profile);
    }

    @Test
    public void testGetUserProfileByScreenName() {
        String screenName = "Cirrus_Test1";
        mockServer
                .expect(requestTo("https://api.weibo.com/2/users/show.json?screen_name="
                        + screenName))
                .andExpect(method(GET))
                .andRespond(
                        withSuccess(jsonResource("profile"), MediaType.APPLICATION_JSON));

        WeiboProfile profile = userTemplate
                .getUserProfileByScreenName(screenName);
        verifyWeiboProfile(profile);
    }

}
