package com.sublime.whatsappclone.infrastructure.primary.user;

import com.sublime.whatsappclone.messaging.application.UserApplicationService;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @GetMapping("/get-last-seen")
    public ResponseEntity<Instant> getLastSeen(@RequestParam UUID publicId) {
        Optional<Instant> lastSeen = userApplicationService.getLastSeen(new UserPublicId(publicId));
        if (lastSeen.isPresent()) {
            return ResponseEntity.ok(lastSeen.get());
        } else {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                    "Unable to fetch the presence of the user " + publicId);
            return ResponseEntity.of(problemDetail).build();
        }

    }
}
