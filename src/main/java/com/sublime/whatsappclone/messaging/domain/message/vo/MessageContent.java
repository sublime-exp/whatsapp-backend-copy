package com.sublime.whatsappclone.messaging.domain.message.vo;


import lombok.Builder;

@Builder
public record MessageContent(String text,
                             MessageType type,
                             MessageMediaContent media) {
}
