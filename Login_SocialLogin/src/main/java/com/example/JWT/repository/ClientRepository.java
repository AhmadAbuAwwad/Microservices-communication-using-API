package com.example.JWT.repository;

import com.example.JWT.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String username);

//    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
