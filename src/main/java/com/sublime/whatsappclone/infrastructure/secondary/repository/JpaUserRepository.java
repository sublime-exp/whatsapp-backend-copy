package com.sublime.whatsappclone.infrastructure.secondary.repository;

import com.sublime.whatsappclone.infrastructure.secondary.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
