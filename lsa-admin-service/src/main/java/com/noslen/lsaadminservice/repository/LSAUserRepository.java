package com.noslen.lsaadminservice.repository;


import com.noslen.lsaadminservice.model.LSAUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LSAUserRepository extends JpaRepository<LSAUser, Integer> {

    LSAUser findByUsername(String username);
}
