package org.wefresh.wefresh_server.openAi.dto.response;

import java.util.List;

public record GptVisionResponseDto(
        List<Choice> choices
) {
    public record Choice(Message message) {
        public record Message(String content) {}
    }
}

