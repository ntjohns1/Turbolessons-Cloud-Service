package com.noslen.lsaadminservice.repository;


import com.noslen.lsaadminservice.model.LSAUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LSAUserRepository extends JpaRepository<LSAUser, Integer> {
}
