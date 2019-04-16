package com.customerService.app.model.dao;

import com.customerService.app.model.entity.RealPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface RealPersonDao extends JpaRepository<RealPersonEntity,Integer> {

    RealPersonEntity findByNationalCode(String nCode);
    @Query("SELECT p FROM RealPersonEntity p WHERE p.name like %:name%")
    List<RealPersonEntity> findByName(@Param("name")String name);
}
