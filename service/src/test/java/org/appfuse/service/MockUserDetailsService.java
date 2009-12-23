package org.appfuse.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.appfuse.model.User;
import org.springframework.dao.DataAccessException;

public class MockUserDetailsService implements UserDetailsService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        return new User("testuser");
    }
}
