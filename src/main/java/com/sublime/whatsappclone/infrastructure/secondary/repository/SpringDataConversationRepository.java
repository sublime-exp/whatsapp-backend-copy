package com.sublime.whatsappclone.infrastructure.secondary.repository;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.ConversationToCreate;
import com.sublime.whatsappclone.messaging.domain.message.repository.ConversationRepository;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SpringDataConversationRepository implements ConversationRepository {

    @Override
    public Conversation save(ConversationToCreate conversation, List<User> members) {
        return null;
    }

    @Override
    public Optional<Conversation> get(ConversationPublicId id) {
        return Optional.empty();
    }

    @Override
    public Page<Conversation> getConversationByUserPublicId(UserPublicId id, Pageable pageable) {
        return null;
    }

    @Override
    public int deleteByPublicId(UserPublicId userPublicId, ConversationPublicId conversationPublicId) {
        return 0;
    }

    @Override
    public Optional<Conversation> getConversationByUsersPublicIdAndPubicId(UserPublicId userPublicId, ConversationPublicId conversationPublicId) {
        return Optional.empty();
    }

    @Override
    public Optional<Conversation> getConversationByUsersPublicIds(List<UserPublicId> publicIds) {
        return Optional.empty();
    }

    @Override
    public Optional<Conversation> getOneByPublicId(ConversationPublicId publicId) {
        return Optional.empty();
    }
}
