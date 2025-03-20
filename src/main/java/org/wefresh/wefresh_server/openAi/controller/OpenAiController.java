package org.wefresh.wefresh_server.openAi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.openAi.dto.response.gpt.RecommendRecipesDto;
import org.wefresh.wefresh_server.openAi.dto.response.gpt.TodayRecipesDto;
import org.wefresh.wefresh_server.openAi.service.OpenAiService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAiService openAiService;

    @GetMapping("/recipes")
    public ResponseEntity<ResponseDto<RecommendRecipesDto>> getRecipe(
            @UserId final Long userId,
            @RequestParam final List<Long> foodIds
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(openAiService.getRecipe(userId, foodIds)));
    }

    @GetMapping("/recipes/today")
    public ResponseEntity<ResponseDto<TodayRecipesDto>> getTodayRecipes(
            @UserId final Long userId
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(openAiService.getTodayRecipes(userId)));
    }
}
