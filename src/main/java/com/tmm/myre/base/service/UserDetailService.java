/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tmm.myre.base.model.AppUser;
import com.tmm.myre.base.model.MyreSessionLog;
import com.tmm.myre.base.repository.IAppRoleRepository;
import com.tmm.myre.base.repository.IAppUserRepository;
import com.tmm.myre.base.repository.ISessionLogRepository;
import com.tmm.myre.base.utils.DateManagement;
import com.tmm.myre.base.utils.UuidProvider;

@Service("userDetailService") 
public class UserDetailService implements UserDetailsService{

	@Autowired
	private IAppUserRepository appUserRepository;
	
	@Autowired
	private IAppRoleRepository appRoleRepository;
	
	@Autowired
	private ISessionLogRepository sessionLogRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepository.findByUserName(username);
		if(appUser == null) {
			System.out.println("[**********]User ->" + username + "<- not found...");
			throw new UsernameNotFoundException(username);
		}
		System.out.println("[**********]User ->" + appUser + "<- Founded");

		List<String> roleNames = appRoleRepository.getRoleNames(appUser.getUserId());
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				// ROLE_USER, ROLE_ADMIN,..
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}
		System.out.println("[**********]User ->" + roleNames.toString() + "<- Founded");
		UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), //
				appUser.getEncrytedPassword(), grantList);
		
		/* LOG SESSIONS */
		MyreSessionLog session = MyreSessionLog.builder()
				.sessionId(UuidProvider.getUUID())
				.userId(appUser.getUserId())
				.sessionDatetime(DateManagement.getTodayTimestamp())
				.build();
		sessionLogRepository.save(session);
		
		return userDetails;
	}

	public Integer getUserId(String userName) {
		AppUser appUser = appUserRepository.findByUserName(userName);
		if(appUser == null) {
			return Integer.valueOf("99999");
		} else {
			return Integer.valueOf(appUser.getUserId());
		}
	}
	
}
