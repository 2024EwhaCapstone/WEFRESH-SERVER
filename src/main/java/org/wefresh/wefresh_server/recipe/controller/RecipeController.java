package org.wefresh.wefresh_server.recipe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.recipe.dto.response.RecipeDto;
import org.wefresh.wefresh_server.recipe.service.RecipeService;

@RestController
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/recipes/{id}")
    public ResponseEntity<ResponseDto<RecipeDto>> getRecipe(
            @UserId final Long userId,
            @PathVariable final Long id,
            @RequestParam(required = false) final String type
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(recipeService.getRecipe(userId, id, type)));
    }
}
