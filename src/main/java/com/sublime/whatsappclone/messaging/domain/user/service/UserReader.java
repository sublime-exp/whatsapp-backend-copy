package com.sublime.whatsappclone.messaging.domain.user.service;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.repository.UserRepository;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;

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
}
