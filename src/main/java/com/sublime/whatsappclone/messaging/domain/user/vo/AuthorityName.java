package com.sublime.whatsappclone.messaging.domain.user.vo;

import com.sublime.whatsappclone.shared.error.domain.Assert;

public record AuthorityName(String name) {

    public AuthorityName {
        Assert.field("name", name).notNull();
    }
}
