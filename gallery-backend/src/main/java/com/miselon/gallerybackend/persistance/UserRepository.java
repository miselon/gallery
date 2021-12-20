package com.miselon.gallerybackend.persistance;

import com.miselon.gallerybackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Modifying
    @Query("update user u set u.enabled = true where u.id = :id")
    int enableUserAccount(@Param("id") String id);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
