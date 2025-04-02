package com.sublime.whatsappclone.infrastructure.secondary.entity;

import com.sublime.whatsappclone.messaging.domain.message.vo.MessageContent;
import com.sublime.whatsappclone.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "message_binary_content")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageContentBinaryEntity extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "messageContentBinarySequenceGenerator")
    @SequenceGenerator(
            name = "messageContentBinarySequenceGenerator",
            sequenceName = "message_binary_content_sequence",
            allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "file", nullable = false)
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @OneToOne(mappedBy = "contentBinary")
    private MessageEntity message;


    public static MessageContentBinaryEntity from(MessageContent messageContent) {
        return MessageContentBinaryEntity.builder()
                .fileContentType(messageContent.media().mimeType())
                .file(messageContent.media().file())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageContentBinaryEntity that = (MessageContentBinaryEntity) o;
        return Objects.deepEquals(file, that.file) && Objects.equals(fileContentType, that.fileContentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(file), fileContentType);
    }
}
