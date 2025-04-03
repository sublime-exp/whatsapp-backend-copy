package com.sublime.whatsappclone.messaging.domain.message.service;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.repository.ConversationRepository;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ConversationReader {

    private final ConversationRepository repository;

    public ConversationReader(ConversationRepository repository) {
        this.repository = repository;
    }

    public Page<Conversation> getAllByUserPublicID(UserPublicId userPublicId,
                                                   Pageable pageable) {
        return repository.getConversationByUserPublicId(userPublicId, pageable);
    }

    public Optional<Conversation> getOneByPublicId(ConversationPublicId conversationPublicId) {
        return repository.getOneByPublicId(conversationPublicId);
    }


    public Optional<Conversation> getOneByPublicIdAndUserId(ConversationPublicId conversationPublicId,
                                                            UserPublicId userPublicId) {
        return repository.getConversationByUsersPublicIdAndPublicId(userPublicId, conversationPublicId);
    }


}
