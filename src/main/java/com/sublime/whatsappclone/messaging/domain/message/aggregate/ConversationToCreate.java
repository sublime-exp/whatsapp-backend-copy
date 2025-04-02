package com.sublime.whatsappclone.messaging.domain.message.aggregate;

import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationName;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.error.domain.Assert;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class ConversationToCreate {

    private final Set<UserPublicId> members;

    private final ConversationName name;

    public ConversationToCreate(Set<UserPublicId> members, ConversationName name) {
        assertMandatoryFields(members, name);
        this.members = members;
        this.name = name;
    }

    private void assertMandatoryFields(Set<UserPublicId> members, ConversationName name) {
        Assert.notNull("name", name);
        Assert.notNull("members", members);
    }

}