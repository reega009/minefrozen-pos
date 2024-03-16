package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.UserDao;
import com.minefrozen.pos.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao dao;

    public Optional<UserDto.UserLogin> findByUsername(String username) {
            return dao.findByUsername(username);
    }

}
