package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dao.UserDao;
import com.minefrozen.pos.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao dao;

    @Autowired
    private TrnomaxDao noMaxDao;

    public Optional<UserDto.UserLogin> findByUsername(String username) {
            return dao.findByUsername(username);
    }


    public List<UserDto.User> findAll(){
        return dao.findAll();
    }

    public Optional<UserDto.User> findById(Integer id){
        return dao.findById(id);
    }

    @Transactional("posTransaction")
    public void save(UserDto.User data){
        // Get Nomax
        Integer newId = noMaxDao.findNoMax("truser");
        data.setId(newId);

        // Set DPgunRekam
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        data.setDPgunRekam(currentTimestamp);

        dao.save(data);
        // Update Nomax
        noMaxDao.updateTrNomax("truser");
    }

    @Transactional("posTransaction")
    public void update(UserDto.User data){
        dao.update(data);
    }

    public void delete(Integer id){
        dao.delete(id);
    }


}
