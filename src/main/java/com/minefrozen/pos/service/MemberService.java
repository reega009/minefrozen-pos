package com.minefrozen.pos.service;

import com.minefrozen.pos.dao.MemberDao;
import com.minefrozen.pos.dao.TrnomaxDao;
import com.minefrozen.pos.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberDao dao;

    @Autowired
    private TrnomaxDao nomaxDao;

    public List<MemberDto.Member> findAll(Integer idStore){
        return dao.findAll(idStore);
    }

    public Optional<MemberDto.Member> findById(Integer id, Integer idStore){
        return dao.findById(id, idStore);
    }


    public Optional<MemberDto.Member> findByNameOrKodeMember(Integer idStore, String namaMember, String kodeMember){
        return dao.findByNameOrKodeMember(idStore, namaMember, kodeMember);
    }

    @Transactional("posTransaction")
    public void save(MemberDto.Member data){
        Integer newId = nomaxDao.findNoMax("tmmember");
        String newKode = String.format("MBR-%06d", newId);
        data.setId(newId);
        data.setKodeMember(newKode);
        dao.save(data);
        nomaxDao.updateTrNomax("tmmember");
    }

    public void update(MemberDto.Member data){
        dao.update(data);
    }

    public void delete(Integer id){
        dao.delete(id);
    }

}
