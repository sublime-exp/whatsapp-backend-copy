package com.sublime.whatsappclone.infrastructure.primary;

import com.sublime.whatsappclone.messaging.application.UserApplicationService;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserApplicationService userApplicationService;

    @GetMapping("get-authenticated-user")
    ResponseEntity<RestUser> getAuthenticatedUser(@AuthenticationPrincipal Jwt user,
                                                  @RequestParam boolean forceResync) {
        User authenticated = userApplicationService.getAuthenticatedUserWithSync(user, forceResync);
        RestUser restUser = RestUser.from(authenticated);
        return ResponseEntity.ok(restUser);
    }
}
