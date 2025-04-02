package com.sublime.whatsappclone.messaging.domain.user.vo;

import com.sublime.whatsappclone.shared.error.domain.Assert;

public record UserLastName(String value) {

    public UserLastName {
        Assert.field(value, value).maxLength(255);
    }
}