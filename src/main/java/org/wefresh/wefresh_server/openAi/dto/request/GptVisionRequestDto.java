package org.wefresh.wefresh_server.openAi.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record GptVisionRequestDto(
        String model,
        List<Message> messages
) {
    public static GptVisionRequestDto of(String model, String imageUrl, String prompt) {
        return new GptVisionRequestDto(
                model,
                List.of(new Message("user", List.of(
                        Content.text(prompt),
                        Content.imageUrl(imageUrl)
                )))
        );
    }

    public record Message(String role, List<Content> content) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Content(
            String type,
            String text,
            ImageUrl image_url
    ) {
        public static Content text(String prompt) {
            return new Content("text", prompt, null);
        }

        public static Content imageUrl(String url) {
            return new Content("image_url", null, new ImageUrl(url));
        }
    }

    public record ImageUrl(String url) {}
}
