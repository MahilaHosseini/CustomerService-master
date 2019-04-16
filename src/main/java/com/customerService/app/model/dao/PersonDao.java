package com.customerService.app.model.dao;

import com.customerService.app.model.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDao extends JpaRepository<PersonEntity,Integer> {


}
