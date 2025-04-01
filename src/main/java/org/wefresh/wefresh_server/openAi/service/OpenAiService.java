package org.wefresh.wefresh_server.openAi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.manager.FoodRetriever;
import org.wefresh.wefresh_server.openAi.dto.request.GptRequestDto;
import org.wefresh.wefresh_server.openAi.dto.response.gpt.GptRecipeResponseDto;
import org.wefresh.wefresh_server.openAi.dto.response.gpt.GptResponseDto;
import org.wefresh.wefresh_server.openAi.dto.response.gpt.RecommendRecipesDto;
import org.wefresh.wefresh_server.openAi.dto.response.gpt.TodayRecipesDto;
import org.wefresh.wefresh_server.openAi.util.JsonUtil;
import org.wefresh.wefresh_server.recipe.domain.Recipe;
import org.wefresh.wefresh_server.recipe.manager.RecipeSaver;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;
import org.wefresh.wefresh_server.todayRecipe.manager.TodayRecipeSaver;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.manager.UserRetriever;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api-url}")
    private String apiURL;

    private final RestTemplate restTemplate;
    private final UserRetriever userRetriever;
    private final FoodRetriever foodRetriever;
    private final RecipeSaver recipeSaver;
    private final TodayRecipeSaver todayRecipeSaver;
    private final DallEService dallEService;

    @Transactional
    public RecommendRecipesDto getRecipe(final Long userId, final List<Long> foodIds) {
        User user = userRetriever.findById(userId);
        List<Food> foods = foodRetriever.findByIdIn(foodIds);
        return saveAndReturnRecommendRecipes(foods);
    }

    @Transactional
    public TodayRecipesDto getTodayRecipes(final Long userId) {
        List<Food> randomFoods = foodRetriever.findRandomFoods(userId, 3);
        return saveAndReturnTodayRecipes(randomFoods);
    }

    private RecommendRecipesDto saveAndReturnRecommendRecipes(List<Food> foods) {
        List<Recipe> savedRecipes = saveRecipes(foods);
        return RecommendRecipesDto.from(savedRecipes, foods);
    }

    private TodayRecipesDto saveAndReturnTodayRecipes(List<Food> foods) {
        List<TodayRecipe> savedRecipes = saveTodayRecipes(foods);
        return TodayRecipesDto.from(savedRecipes);
    }

    private List<Recipe> saveRecipes(List<Food> foods) {
        GptRecipeResponseDto gptRecipes = fetchGptRecipes(foods);
        List<Recipe> recipes = convertToRecipes(gptRecipes);
        return recipeSaver.saveAll(recipes);
    }

    private List<TodayRecipe> saveTodayRecipes(List<Food> foods) {
        GptRecipeResponseDto gptRecipes = fetchGptRecipes(foods);
        List<TodayRecipe> todayRecipes = convertToTodayRecipes(gptRecipes);
        return todayRecipeSaver.saveAll(todayRecipes);
    }

    private GptRecipeResponseDto fetchGptRecipes(List<Food> foods) {
        String ingredients = foods.stream().map(Food::getName).collect(Collectors.joining(", "));
        String prompt = generateRecipePrompt(ingredients);

        GptRequestDto request = GptRequestDto.of(model, prompt);
        GptResponseDto responseDto = restTemplate.postForObject(apiURL, request, GptResponseDto.class);
        String jsonResponse = extractJson(responseDto.choices().get(0).message().content());

        return JsonUtil.fromJson(jsonResponse, GptRecipeResponseDto.class);
    }

    private String generateRecipePrompt(String ingredients) {
        return String.format(
                """
                너는 프로 요리사야.  
                %s가 포함된 요리 3개를 JSON 형식으로 추천해줘. 
                각 레시피에는 반드시 모든 재료가 포함되어야 해.  
                각 재료의 적절한 양(단위 포함)도 반드시 포함해야 해.                    
                
                **응답 형식 (JSON)**
                ```json
                {
                  "recipes": [
                    {
                      "name": "요리 이름",
                      "ingredients": ["연어 200g", "새우 200g", "감자 2알"],
                      "time": 30,
                      "calorie": 500,
                      "difficulty": 3,
                      "steps": ["Step 1. 재료를 준비한다.", "Step 2. 연어를 열심히 볶는다.", "Step 3. 먹는다."]
                    },
                    {
                      "name": "다른 요리",
                      "ingredients": [...],
                      "time": ...,
                      "calorie": ...,
                      "difficulty": ...,
                      "steps": [...]
                    }
                  ]
                }
                ```
                위 JSON 형식으로 **정확하게** 3개의 레시피 응답해줘.
                모든 레시피에는 반드시 [%s]가 포함되어야 해.
                JSON 외에 불필요한 문장은 절대 포함하지 마.
                """, ingredients, ingredients);
    }

    private List<Recipe> convertToRecipes(GptRecipeResponseDto gptRecipes) {
        return gptRecipes.recipes().stream()
                .map(dto -> Recipe.builder()
                        .name(dto.name())
                        .image(dallEService.generateAndUploadImage(dto.name()))
                        .time(dto.time())
                        .calorie(dto.calorie())
                        .difficulty(dto.difficulty())
                        .likeCount(0)
                        .ingredients(String.join(", ", dto.ingredients()))
                        .recipe(String.join("\n", dto.steps()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<TodayRecipe> convertToTodayRecipes(GptRecipeResponseDto gptRecipes) {
        return gptRecipes.recipes().stream()
                .map(dto -> TodayRecipe.builder()
                        .name(dto.name())
                        .image(dallEService.generateAndUploadImage(dto.name()))
                        .time(dto.time())
                        .calorie(dto.calorie())
                        .difficulty(dto.difficulty())
                        .ingredients(String.join(", ", dto.ingredients()))
                        .recipe(String.join("\n", dto.steps()))
                        .build())
                .collect(Collectors.toList());
    }

    private String extractJson(String response) {
        int jsonStart = response.indexOf("{");
        if (jsonStart != -1) {
            return response.substring(jsonStart);
        }
        throw new RuntimeException("JSON 형식을 찾을 수 없음");
    }
}
