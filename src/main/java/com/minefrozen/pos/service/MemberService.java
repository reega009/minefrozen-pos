package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.MemberDao;
import com.minefrozen.pos.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberDao dao;

    public List<MemberDto.Member> findAll(Integer idStore){
        return dao.findAll(idStore);
    }

    public Optional<MemberDto.Member> findById(Integer id, Integer idStore){
        return dao.findById(id, idStore);
    }


    public Optional<MemberDto.Member> findByNameOrKodeMember(Integer idStore, String namaMember, String kodeMember){
        return dao.findByNameOrKodeMember(idStore, namaMember, kodeMember);
    }

    public void save(MemberDto.Member data){
        dao.save(data);
    }

    public void update(MemberDto.Member data){
        dao.update(data);
    }

    public void delete(Integer id){
        dao.delete(id);
    }

}
