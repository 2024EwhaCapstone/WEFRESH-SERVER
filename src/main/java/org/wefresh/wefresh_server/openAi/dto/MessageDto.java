package org.wefresh.wefresh_server.openAi.dto;

public record MessageDto(
        String role,
        String content
) {
    public static MessageDto from(String role, String content) {
        return new MessageDto(role, content);
    }
}
