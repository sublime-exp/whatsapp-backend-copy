package com.sublime.whatsappclone.messaging.domain.user.vo;

import com.sublime.whatsappclone.shared.error.domain.Assert;

public record UserFirstname(String value) {

    public UserFirstname {
        Assert.field(value, value).maxLength(255);
    }
}