package com.sublime.whatsappclone.infrastructure.primary.user;

import com.sublime.whatsappclone.messaging.application.UserApplicationService;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserApplicationService userApplicationService;

    @GetMapping("get-authenticated-user")
    public ResponseEntity<RestUser> getAuthenticatedUser(@AuthenticationPrincipal Jwt user,
                                                         @RequestParam boolean forceResync) {
        User authenticated = userApplicationService.getAuthenticatedUserWithSync(user, forceResync);
        RestUser restUser = RestUser.from(authenticated);
        return ResponseEntity.ok(restUser);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestSearchUser>> search(Pageable pageable,
                                                       @RequestParam String query) {
        List<RestSearchUser> searchResults = userApplicationService
                .search(pageable, query)
                .stream()
                .map(RestSearchUser::from)
                .toList();
        return ResponseEntity.ok(searchResults);
    }
}
