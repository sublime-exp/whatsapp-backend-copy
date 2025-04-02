package com.sublime.whatsappclone.messaging.domain.user.aggregate;

import com.sublime.whatsappclone.messaging.domain.user.vo.AuthorityName;
import com.sublime.whatsappclone.shared.error.domain.Assert;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Authority {

    private AuthorityName name;

    public Authority(AuthorityName name) {
        Assert.notNull("name", name);
        this.name = name;
    }

}
