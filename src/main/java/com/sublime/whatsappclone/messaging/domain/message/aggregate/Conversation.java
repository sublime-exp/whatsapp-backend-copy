package com.sublime.whatsappclone.messaging.domain.message.aggregate;

import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationName;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.shared.error.domain.Assert;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class Conversation {

    private final Set<Message> messages;

    private final Set<User> members;

    private final ConversationPublicId conversationPublicId;

    private final ConversationName conversationName;

    private Long dbId;

    public Conversation(Set<Message> messages,
                        Set<User> members,
                        ConversationPublicId conversationPublicId,
                        ConversationName conversationName,
                        Long dbId) {
        assertMandatoryFields(members, conversationName);
        this.messages = messages;
        this.members = members;
        this.conversationPublicId = conversationPublicId;
        this.conversationName = conversationName;
        this.dbId = dbId;
    }

    private void assertMandatoryFields(Set<User> users, ConversationName name) {
        Assert.notNull("users", users);
        Assert.notNull("name", name);
    }
}
