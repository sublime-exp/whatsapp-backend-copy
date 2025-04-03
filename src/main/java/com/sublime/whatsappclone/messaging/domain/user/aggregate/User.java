package com.sublime.whatsappclone.messaging.domain.user.aggregate;

import com.sublime.whatsappclone.messaging.domain.user.vo.*;
import com.sublime.whatsappclone.shared.error.domain.Assert;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class User {

    private UserLastName lastName;

    private UserFirstname firstname;

    private UserEmail email;

    private UserPublicId userPublicId;

    private UserImageUrl imageUrl;

    private Instant lastModifiedDate;

    private Instant createdDate;

    private Instant lastSeen;

    private Set<Authority> authorities;

    private Long dbId;

    public User(UserLastName lastName,
                UserFirstname firstname,
                UserEmail email,
                UserPublicId userPublicId,
                UserImageUrl imageUrl,
                Instant lastModifiedDate,
                Instant createdDate,
                Instant lastSeen,
                Set<Authority> authorities,
                Long dbId) {
        assertMandatoryFields(lastName, firstname, email, authorities);
        this.lastName = lastName;
        this.firstname = firstname;
        this.email = email;
        this.userPublicId = userPublicId;
        this.imageUrl = imageUrl;
        this.lastModifiedDate = lastModifiedDate;
        this.createdDate = createdDate;
        this.lastSeen = lastSeen;
        this.authorities = authorities;
        this.dbId = dbId;
    }

    private void assertMandatoryFields(UserLastName lastName,
                                       UserFirstname firstname,
                                       UserEmail email,
                                       Set<Authority> authorities) {
        Assert.notNull("lastName", lastName);
        Assert.notNull("firstname", firstname);
        Assert.notNull("email", email);
        Assert.notNull("authorities", authorities);
    }

    public void updateFromUser(User user) {
        this.email = user.email;
        this.lastName = user.lastName;
        this.firstname = user.firstname;
        this.imageUrl = user.imageUrl;
    }

    public static User fromTokenAttributes(Map<String, Object> attributes,
                                           List<String> rolesFromAccessToken) {
        UserBuilder builder = User.builder();

        String sub = String.valueOf(attributes.get("sub"));

        String username = null;

        if (attributes.containsKey("preferred_username")) {
            username = attributes.get("preferred_username").toString().toLowerCase();
        }

        if (attributes.containsKey("given_name")) {
            builder.firstname(new UserFirstname(attributes.get("given_name").toString()));
        } else if (attributes.containsKey("nickname")) {
            builder.firstname(new UserFirstname(attributes.get("nickname").toString()));
        }

        if (attributes.containsKey("family_name")) {
            builder.lastName(new UserLastName(attributes.get("family_name").toString()));
        }

        if (attributes.containsKey("email")) {
            builder.email(new UserEmail(attributes.get("email").toString()));
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            builder.email(new UserEmail(username));
        } else {
            builder.email(new UserEmail(sub));
        }

        if (attributes.containsKey("image_url")) {
            builder.imageUrl(new UserImageUrl(attributes.get("image_url").toString()));
        }

        Set<Authority> authorities = rolesFromAccessToken
                .stream()
                .map(authority -> Authority.builder()
                        .name(new AuthorityName(authority))
                        .build())
                .collect(Collectors.toSet());

        builder.authorities(authorities);

        return builder.build();
    }

    public void initFieldForSignup() {
        this.lastSeen = Instant.now();
    }

}
