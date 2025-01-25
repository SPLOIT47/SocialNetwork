package com.socialnetwork.usermicroservice.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.socialnetwork.usermicroservice.entity.SnUserDetail;
import com.socialnetwork.usermicroservice.entity.RoleEntity;
import com.socialnetwork.usermicroservice.entity.UserEntity;
import com.socialnetwork.usermicroservice.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public SnUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByNickname(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username));

        Set<RoleEntity> roles = user.getRoles();
        
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }   
        
        SnUserDetail customUserDetail = new SnUserDetail();
        customUserDetail.setUser(user);
        customUserDetail.setAuthorities(authorities);

        return customUserDetail;
    }

    public SnUserDetail loadUserById(String id) throws UsernameNotFoundException {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + id));

        Set<RoleEntity> roles = user.getRoles();

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (RoleEntity role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        SnUserDetail customUserDetail = new SnUserDetail();
        customUserDetail.setUser(user);
        customUserDetail.setAuthorities(authorities);

        return customUserDetail;
    }
}
