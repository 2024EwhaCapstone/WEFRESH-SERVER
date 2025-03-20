package org.wefresh.wefresh_server.openAi.dto.request;

import org.wefresh.wefresh_server.openAi.dto.MessageDto;
import java.util.List;

public record GptRequestDto(
        String model,
        List<MessageDto> messages
) {
    public static GptRequestDto of(String model, String prompt) {
        return new GptRequestDto(
                model,
                List.of(MessageDto.from("user", prompt))
        );
    }
}
