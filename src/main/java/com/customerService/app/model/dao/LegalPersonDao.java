package com.customerService.app.model.dao;

import com.customerService.app.model.entity.LegalPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface LegalPersonDao extends JpaRepository<LegalPersonEntity,Integer> {
   LegalPersonEntity findByRegistrationCode(String rCode);
   @Query("SELECT p FROM LegalPersonEntity p WHERE p.name like %:name%")
   List<LegalPersonEntity> findByName(@Param("name")String name);

}
