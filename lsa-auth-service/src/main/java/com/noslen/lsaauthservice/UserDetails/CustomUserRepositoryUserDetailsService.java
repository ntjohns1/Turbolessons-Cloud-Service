/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.noslen.lsaauthservice.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
//import com.noslen.lsaauthservice.repository.CustomUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noslen.lsaauthservice.model.CustomUser;
import com.noslen.lsaauthservice.util.feign.AdminClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

@Service
public class CustomUserRepositoryUserDetailsService implements UserDetailsService {

	@Autowired
	private WebApplicationContext applicationContext;

	@Autowired
	private AdminClient userRepository;


//	@PostConstruct
//	public void completeSetup() {
//		userRepository = applicationContext.getBean(CustomUserRepository.class);
//	}
//	public CustomUserRepositoryUserDetailsService(CustomUserRepository userRepository) {
//		this.userRepository = userRepository;
//	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Object obj = this.userRepository.getUserByUsername(username);
		ObjectMapper objectMapper = new ObjectMapper();
		String s = null;
		try {
			s = objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		JSONObject userObj = new JSONObject(s);
		int id = Integer.parseInt(userObj.get("id").toString());
		CustomUser customUser = new CustomUser(id, userObj.get("email").toString(), userObj.get("username").toString(), userObj.get("password").toString());
		if (customUser == null) {
			throw new UsernameNotFoundException("username " + username + " is not found");
		}
		return new CustomUserDetails(customUser);
	}

	static final class CustomUserDetails extends CustomUser implements UserDetails {

		private static final List<GrantedAuthority> ROLE_USER = Collections
				.unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

		CustomUserDetails(CustomUser customUser) {
			super(customUser.getId(), customUser.getEmail(), customUser.getUsername(), customUser.getPassword());
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return ROLE_USER;
		}

		@Override
		public String getUsername() {
			return getEmail();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

	}

}
