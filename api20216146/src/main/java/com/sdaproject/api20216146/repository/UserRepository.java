package com.sdaproject.api20216146.repository;

import com.sdaproject.api20216146.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); 
    User findByEmail(String email); 
    boolean existsByUsername(String username); 
    boolean existsByEmail(String email); 
}
