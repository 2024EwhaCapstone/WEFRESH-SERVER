package org.wefresh.wefresh_server.openAi.dto.response.gpt;

import org.wefresh.wefresh_server.openAi.dto.MessageDto;

import java.util.List;

public record GptResponseDto(
        List<ChoiceDto> choices
) {
    public record ChoiceDto(
            int index,
            MessageDto message
    ) {
    }
}

