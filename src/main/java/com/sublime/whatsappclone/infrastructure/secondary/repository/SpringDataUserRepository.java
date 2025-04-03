package com.sublime.whatsappclone.infrastructure.secondary.repository;

import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.repository.UserRepository;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class SpringDataUserRepository implements UserRepository {
    @Override
    public void save(User user) {

    }

    @Override
    public Optional<User> get(UserPublicId id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getOneByEmail(UserEmail email) {
        return Optional.empty();
    }

    @Override
    public List<User> getByPublicIds(List<UserPublicId> publicIds) {
        return List.of();
    }

    @Override
    public Page<User> search(Pageable pageable, String query) {
        return null;
    }

    @Override
    public int updateLastSeenByPublicId(UserPublicId publicId, Instant lastSeen) {
        return 0;
    }

    @Override
    public List<User> getRecipientsByConversationIdExcludingReader(ConversationPublicId conversationId, UserPublicId readerPublicId) {
        return List.of();
    }
}
