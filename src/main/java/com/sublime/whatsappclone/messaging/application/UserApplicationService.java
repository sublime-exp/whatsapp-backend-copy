package com.sublime.whatsappclone.messaging.application;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.repository.UserRepository;
import com.sublime.whatsappclone.messaging.domain.user.service.UserReader;
import com.sublime.whatsappclone.messaging.domain.user.service.UserSynchronizer;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.shared.authentication.application.AuthenticatedUser;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserApplicationService {

    private final UserSynchronizer userSynchronizer;

    private final UserReader userReader;

    public UserApplicationService(UserRepository userRepository) {
        this.userSynchronizer = new UserSynchronizer(userRepository);
        this.userReader = new UserReader(userRepository);
    }

    @Transactional
    public User getAuthenticatedUserWithSync(Jwt oauth2User, boolean forceResync) {
        userSynchronizer.syncWithIdp(oauth2User, forceResync);
        return userReader
                .getByEmail(new UserEmail(AuthenticatedUser.username().get()))
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        return userReader
                .getByEmail(new UserEmail(AuthenticatedUser.username().get()))
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<User> search(Pageable pageable, String query) {
        return userReader.search(pageable, query).stream().toList();
    }

}
