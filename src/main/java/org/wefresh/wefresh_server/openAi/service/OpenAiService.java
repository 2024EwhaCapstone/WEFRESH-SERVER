package org.wefresh.wefresh_server.openAi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.manager.FoodRetriever;
import org.wefresh.wefresh_server.openAi.dto.request.GptRequestDto;
import org.wefresh.wefresh_server.openAi.dto.request.GptVisionRequestDto;
import org.wefresh.wefresh_server.openAi.dto.response.GptVisionResponseDto;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpenAiService {
    @Value("${openai.models.text}")
    private String textModel;

    @Value("${openai.models.vision}")
    private String visionModel;

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

        GptRequestDto request = GptRequestDto.of(textModel, prompt);
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

    // 신선도 분석
    public String analyzeFreshness(String name, String imageUrl) {
        String prompt = String.format("""
        다음 사진은 "%s"라는 음식이야. 이 음식 사진을 보고 신선도 상태와 문제 항목들을 분석해줘.
        
        - 전체 신선도 상태는 "매우 신선함", "보통", "상함" 중 하나로 판단해줘.
        - 문제가 의심되는 항목이 여러 개 보이면, 최대 2~3개 정도로 추려서 요약 제목(summary)과 설명(description)을 포함해줘.
        - 응답은 반드시 아래 JSON 형식으로 해줘. 다른 문장, 인삿말, 코드블럭(```json)은 절대 포함하지 마.
        
        {
          "freshness": "상함",
          "reasons": [
            {
              "summary": "하얀 점 (곰팡이일 가능성)",
              "description": "김치 표면에 하얀 점이 다수 보이며, 곰팡이일 가능성이 있음"
            },
            {
              "summary": "색이 변하고 흐물거림",
              "description": "김치가 전체적으로 흐물거리며 색이 탁해졌음"
            }
          ]
        }
        """, name);

        GptVisionRequestDto request = GptVisionRequestDto.of(visionModel, imageUrl, prompt);
        GptVisionResponseDto response = restTemplate.postForObject(apiURL, request, GptVisionResponseDto.class);

        return extractJson(response.choices().get(0).message().content());
    }


}
