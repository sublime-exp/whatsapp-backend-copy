package com.sublime.whatsappclone.messaging.domain.user.vo;

import com.sublime.whatsappclone.shared.error.domain.Assert;

public record UserEmail(String value) {

    public UserEmail {
        Assert.field("value", value).maxLength(255);
    }
}
