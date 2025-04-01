package org.wefresh.wefresh_server.openAi.dto.response.gpt;

import org.wefresh.wefresh_server.openAi.util.JsonUtil;

import java.util.List;

public record GptRecipeResponseDto(
        List<GptRecipeDto> recipes
) {
    public static GptRecipeResponseDto fromJson(String json) {
        return JsonUtil.fromJson(json, GptRecipeResponseDto.class);
    }

    public record GptRecipeDto(
            String name,
            List<String> ingredients,
            int time,
            int calorie,
            int difficulty,
            List<String> steps
    ) {
    }
}


