package com.sublime.whatsappclone.messaging.domain.user.service;

import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.repository.UserRepository;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserReader {

    private final UserRepository userRepository;

    public UserReader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getByEmail(UserEmail email) {
        return userRepository.getOneByEmail(email);
    }

    public List<User> getUsersByPublicId(Set<UserPublicId> publicIds) {
        return userRepository.getByPublicIds(publicIds);
    }

    public Page<User> search(Pageable pageable, String query) {
        return userRepository.search(pageable, query);
    }

    public Optional<User> getByPublicId(UserPublicId userPublicId) {
        return userRepository.getByPublicId(userPublicId);
    }

    public List<User> findUsersToNotify(ConversationPublicId conversationPublicId, UserPublicId readerPublicId) {
        return userRepository.getRecipientByConversationIdExcludingReader(conversationPublicId, readerPublicId);
    }
}
