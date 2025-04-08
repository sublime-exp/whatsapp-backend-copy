package com.sublime.whatsappclone.infrastructure.secondary.repository;

import com.sublime.whatsappclone.infrastructure.secondary.entity.MessageContentBinaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMessageBinaryContent extends JpaRepository<MessageContentBinaryEntity, Long> {
}