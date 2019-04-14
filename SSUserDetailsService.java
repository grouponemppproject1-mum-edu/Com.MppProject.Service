package com.MppProject.services;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.MppProject.models.*;
import com.MppProject.repositories.*;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * This class will implement the UserDetailsService Interface form API has the
 * only method--> public UserDetails loadUserByUsername(String userName) throws
 * UsernameNotFoundException;
 * 
 * the return type is UserDetails type called User class in
 * springframework.security:-> Provides complex Authentication of a user by
 * userName, password, and a set of authority Authority
 * 
 */
@Transactional
public class SSUserDetailsService implements UserDetailsService {
	/**
	 * Recording unusual circumstances or errors that may be happening in the
	 * program A Logger is an object that allows the application to log without
	 * regard to where the output is sent/stored.
	 */

	private static final Logger LOGGER = LoggerFactory.getLogger(SSUserDetailsService.class);
	/**
	 * the userRepostitory will extends to CrudRepository to find the User by name
	 * from database
	 */
	private UserRepository userRepository;

	/**
	 * The constructor will instantiate the userRepository
	 * 
	 * @param userRepository
	 */
	public SSUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	// UserDetails type User-> Provides core user information. after assigned an
	// authentication
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			/**
			 * userRepository will provide us searching by name -> findbyName is the method
			 * in crudeRepostitory to find the name form dataBase
			 */
			User user = userRepository.findByName(username);
			// logging if the user is null, the log is user not fount
			if (user == null) {
				LOGGER.debug("User not found");
				return null;
			}
			LOGGER.debug("User found");

			Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
			/**
			 * SimpleGrantedAuthority :- Authentication -> A granted authority textual
			 * representation predefined Authorities in SimpleGrantedAuthority and we add to
			 * authorities as the form of SimpleGrantedAuthority();
			 */
			authorities.add(new SimpleGrantedAuthority(user.getAuthority()));
			// return the UserDetail
			return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(),
					authorities);

		}
		// if user not found exception if the user is null
		catch (Exception e) {
			throw new UsernameNotFoundException("User not found");
		}
	}

}
