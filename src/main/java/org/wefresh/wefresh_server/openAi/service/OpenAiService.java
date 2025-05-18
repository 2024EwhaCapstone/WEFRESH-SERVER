package org.wefresh.wefresh_server.openAi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.wefresh.wefresh_server.todayRecipe.manager.TodayRecipeRetriever;
import org.wefresh.wefresh_server.todayRecipe.manager.TodayRecipeSaver;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.manager.UserRetriever;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAiService {
    private final TodayRecipeRetriever todayRecipeRetriever;
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
        User user = userRetriever.findById(userId);
        LocalDate today = LocalDate.now();
        List<TodayRecipe> existing = todayRecipeRetriever.findTodayRecipes(userId, today);

        if (!existing.isEmpty()) {
            return TodayRecipesDto.from(existing);
        }

        List<Food> randomFoods = foodRetriever.findRandomFoods(userId, 3);
        return saveAndReturnTodayRecipes(randomFoods, today, user);
    }

    private RecommendRecipesDto saveAndReturnRecommendRecipes(List<Food> foods) {
        List<Recipe> savedRecipes = saveRecipes(foods);
        return RecommendRecipesDto.from(savedRecipes, foods);
    }

    private TodayRecipesDto saveAndReturnTodayRecipes(List<Food> foods, LocalDate date, User user) {
        List<TodayRecipe> savedRecipes = saveTodayRecipes(foods, date, user);
        return TodayRecipesDto.from(savedRecipes);
    }

    private List<Recipe> saveRecipes(List<Food> foods) {
        GptRecipeResponseDto gptRecipes = fetchGptRecipes(foods);
        List<Recipe> recipes = convertToRecipes(gptRecipes);
        return recipeSaver.saveAll(recipes);
    }

    private List<TodayRecipe> saveTodayRecipes(List<Food> foods, LocalDate date, User user) {
        GptRecipeResponseDto gptRecipes = fetchGptRecipes(foods);
        List<TodayRecipe> todayRecipes = convertToTodayRecipes(gptRecipes, date, user);
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

    private List<TodayRecipe> convertToTodayRecipes(GptRecipeResponseDto gptRecipes, LocalDate date, User user) {
        return gptRecipes.recipes().stream()
                .map(dto -> TodayRecipe.builder()
                        .name(dto.name())
                        .image(dallEService.generateAndUploadImage(dto.name()))
                        .time(dto.time())
                        .calorie(dto.calorie())
                        .difficulty(dto.difficulty())
                        .recommendedDate(date)
                        .user(user)
                        .ingredients(String.join(", ", dto.ingredients()))
                        .recipe(String.join("\n", dto.steps()))
                        .build())
                .collect(Collectors.toList());
    }

    private String extractJson(String response) {
        int jsonStart = response.indexOf("{");
        if (jsonStart != -1) {
            String json = response.substring(jsonStart);
            log.info("🔍 GPT 응답 원본:\n{}", response); // 전체 응답 보기
            log.info("📦 추출된 JSON 응답:\n{}", json); // JSON만 보기
            return json;
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
        - reasons(summary, description) 항목은 무조건 최소 1개 있어야해.
        
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

    // 이름, 유통기한 추출
    public String ExtractNameAndExpiration(String imageUrl) {
        String prompt = """
        이 사진에는 식품 제품이 담겨 있어.  
        다음 기준에 따라 **식품명(name)**과 **유통기한(expirationDate)**을 추출해줘.

        - 유통기한은 제품 표기에서 "유통기한", "소비기한", "EXP", "expiration date" 등으로 적혀있는 날짜야.
        - 이름은 제품 포장에 적힌 **가장 큰 제목이나 중심 문구**야. 브랜드명보다 제품명을 우선 추출해줘.
        - 찾지 못한 항목은 `"null"` 또는 `""`로 응답해줘. 오류 문구, 인삿말, 코드블럭(```json)은 절대 포함하지 마.
        - 날짜는 가능한 한 `"YYYY년 MM월 DD일"` 형식으로 정제해줘 (예: 2025년 06월 10일).
        - 응답은 아래 JSON 형식만 사용해. 반드시 key 순서 지켜줘.

        {
            "name": "삼양 불닭볶음면",
            "expirationDate": "2025년 06월 10일"
        }
        """;

        GptVisionRequestDto request = GptVisionRequestDto.of(visionModel, imageUrl, prompt);
        GptVisionResponseDto response = restTemplate.postForObject(apiURL, request, GptVisionResponseDto.class);

        return extractJson(response.choices().get(0).message().content());
    }


}
