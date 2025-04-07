package com.sublime.whatsappclone.messaging.domain.user.repository;

import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository {

    void save(User user);

    Optional<User> get(UserPublicId id);

    Optional<User> getOneByEmail(UserEmail email);

    List<User> getByPublicIds(Set<UserPublicId> publicIds);

    Page<User> search(Pageable pageable, String query);

    int updateLastSeenByPublicId(UserPublicId publicId, Instant lastSeen);

    List<User> getRecipientsByConversationIdExcludingReader(ConversationPublicId conversationId,
                                                            UserPublicId readerPublicId);

    Optional<User> getByPublicId(UserPublicId userPublicId);
}
