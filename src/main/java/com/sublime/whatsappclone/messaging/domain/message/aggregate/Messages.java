package com.sublime.whatsappclone.messaging.domain.message.aggregate;

import com.sublime.whatsappclone.shared.error.domain.Assert;

import java.util.List;

public record Messages(List<Messages> messages) {
    public Messages {
        Assert.field("messages", messages).notNull().noNullElement();
    }
}