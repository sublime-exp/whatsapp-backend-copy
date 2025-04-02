package com.sublime.whatsappclone.messaging.domain.user.vo;

import com.sublime.whatsappclone.shared.error.domain.Assert;

public record UserImageUrl(String value) {

    public UserImageUrl {
        Assert.field(value, value).maxLength(255);
    }
}