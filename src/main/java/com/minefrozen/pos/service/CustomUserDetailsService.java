package com.minefrozen.pos.service;

import com.minefrozen.pos.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDto.UserLogin> user = userService.findByUsername(username);
        List<String> roles = new ArrayList<>();
//        roles.add(user.get().getRole());
        roles.add("USER");
        UserDetails userDetails =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.get().getUsername())
                        .password(user.get().getPassword())
                        .roles(roles.toArray(new String[0]))
                        .build();
        return userDetails;
    }

}
