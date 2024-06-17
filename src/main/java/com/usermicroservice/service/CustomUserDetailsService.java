package com.usermicroservice.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.usermicroservice.entity.CustomUserDetail;
import com.usermicroservice.entity.RoleEntity;
import com.usermicroservice.entity.UserEntity;
import com.usermicroservice.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        Set<RoleEntity> roles = user.getRoles();
        
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }   
        
        CustomUserDetail customUserDeatail = new CustomUserDetail();
        customUserDeatail.setUser(user);
        customUserDeatail.setAuthorities(authorities);

        return customUserDeatail;
    }
}
