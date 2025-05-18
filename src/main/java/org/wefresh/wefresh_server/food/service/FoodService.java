package org.wefresh.wefresh_server.food.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.FoodErrorCode;
import org.wefresh.wefresh_server.external.service.s3.S3Service;
import org.wefresh.wefresh_server.food.domain.Category;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.dto.request.FoodFreshRequestDto;
import org.wefresh.wefresh_server.food.dto.request.FoodImageDto;
import org.wefresh.wefresh_server.food.dto.request.FoodRegisterDto;
import org.wefresh.wefresh_server.food.dto.response.FoodDto;
import org.wefresh.wefresh_server.food.dto.response.FoodListsDto;
import org.wefresh.wefresh_server.food.manager.FoodEditor;
import org.wefresh.wefresh_server.food.manager.FoodRemover;
import org.wefresh.wefresh_server.food.manager.FoodRetriever;
import org.wefresh.wefresh_server.food.manager.FoodSaver;
import org.wefresh.wefresh_server.openAi.dto.response.FreshnessAnalysisDto;
import org.wefresh.wefresh_server.openAi.dto.response.GptVisionResponseDto;
import org.wefresh.wefresh_server.openAi.service.OpenAiService;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.manager.UserRetriever;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final ApplicationContext applicationContext;

    private final S3Service s3Service;

    private final FoodSaver foodSaver;
    private final FoodRetriever foodRetriever;
    private final UserRetriever userRetriever;
    private final OpenAiService openAiService;

    static final String FOOD_S3_UPLOAD_FOLDER = "foods/";
    private final FoodEditor foodEditor;
    private final FoodRemover foodRemover;

    public void registerFood(
            final Long userId,
            final FoodRegisterDto foodRegisterDto
    ) {
        User user = userRetriever.findById(userId);

        String imageUrl = null;
        try {
            imageUrl = s3Service.uploadImage(FOOD_S3_UPLOAD_FOLDER, foodRegisterDto.image());
        } catch (BusinessException e) {
            throw new BusinessException(e.getErrorCode());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        String freshnessJson = openAiService.analyzeFreshness(foodRegisterDto.name(), imageUrl);

        applicationContext.getBean(FoodService.class)
                .saveFood(user, foodRegisterDto, imageUrl, freshnessJson);
    }

    @Transactional(readOnly = true)
    public FoodListsDto getExpiringFood(final Long userId) {
        User user = userRetriever.findById(userId);

        return FoodListsDto.from(foodRetriever.findExpiringByUserId(user.getId()));
    }

    @Transactional(readOnly = true)
    public FoodListsDto getFoods(
            final Long userId,
            String categoryName,
            String name
    ) {
        User user = userRetriever.findById(userId);

        Category category = null;
        if (categoryName != null) {
            category = Category.fromString(categoryName);
        }
        return FoodListsDto.from(foodRetriever.findBySearch(user.getId(), category, name));
    }

    @Transactional(readOnly = true)
    public FoodDto getFood(
            final Long userId,
            final Long foodId
    ) {
        User user = userRetriever.findById(userId);
        Food food = foodRetriever.findById(foodId);

        validateFoodOwner(user.getId(), food);

        return FoodDto.from(food);
    }

    @Transactional
    public void updateFood(
            final Long userId,
            final Long foodId,
            final FoodRegisterDto foodRegisterDto
    ) {
        User user = userRetriever.findById(userId);
        Food food = foodRetriever.findById(foodId);
        validateFoodOwner(user.getId(), food);

        String existingImageUrl = food.getImage();
        String newImageUrl = null;

        // 새로운 이미지가 있으면 업로드
        if (foodRegisterDto.image() != null) {
            try {
                newImageUrl = s3Service.uploadImage(FOOD_S3_UPLOAD_FOLDER, foodRegisterDto.image());
            } catch (IOException e) {
                throw new RuntimeException("새 이미지 업로드 실패: " + e.getMessage());
            }
        }

        // 음식 정보 업데이트 (새 이미지가 없으면 기존 이미지 제거)
        foodEditor.updateFood(
                food,
                newImageUrl,
                foodRegisterDto.name(),
                foodRegisterDto.getCategoryEnum(),
                foodRegisterDto.date(),
                foodRegisterDto.count(),
                foodRegisterDto.memo()
        );

        // 기존 이미지 삭제
        if (existingImageUrl != null && (newImageUrl != null || foodRegisterDto.image() == null)) {
            try {
                s3Service.deleteImage(existingImageUrl);
            } catch (IOException e) {
                System.err.println("기존 이미지 삭제 실패: " + e.getMessage());
            }
        }
    }

    @Transactional
    public void deleteFood(
            final Long userId,
            final Long foodId
    ) {
        User user = userRetriever.findById(userId);
        Food food = foodRetriever.findById(foodId);
        validateFoodOwner(user.getId(), food);

        String existingImageUrl = food.getImage();

        foodRemover.deleteById(food.getId());

        if (existingImageUrl != null) {
            try {
                s3Service.deleteImage(existingImageUrl);
            } catch (IOException e) {
                System.err.println("기존 이미지 삭제 실패: " + e.getMessage());
            }
        }
    }

    @Transactional
    protected void saveFood(User user, FoodRegisterDto foodRegisterDto, String imageUrl, String freshJson) {
        try {
            Food food = Food.builder()
                    .name(foodRegisterDto.name())
                    .image(imageUrl)
                    .category(foodRegisterDto.getCategoryEnum())
                    .date(foodRegisterDto.date())
                    .count(foodRegisterDto.count())
                    .memo(foodRegisterDto.memo())
                    .fresh(freshJson)
                    .user(user)
                    .build();

            foodSaver.save(food);
        } catch (BusinessException e) {
            cleanupUploadedFile(imageUrl);
            throw new BusinessException(e.getErrorCode());
        } catch (RuntimeException e) {
            cleanupUploadedFile(imageUrl);
            throw new RuntimeException("음식 저장 실패: " + e.getMessage());
        }
    }

    private void cleanupUploadedFile(String imageUrl) {
        if (imageUrl != null) {
            try {
                s3Service.deleteImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (BusinessException e) {
                System.err.println("S3 이미지 삭제 실패: " + e.getMessage());
            } catch (RuntimeException e) {
                System.err.println("S3 이미지 삭제 중 알 수 없는 오류 발생: " + e.getMessage());
            }
        }
    }

    private void validateFoodOwner(Long userId, Food food) {
        if (!food.getUser().getId().equals(userId)) {
            throw new BusinessException(FoodErrorCode.FOOD_FORBIDDEN);
        }
    }

    public String getFreshnessJson(
            final Long userId,
            final Long foodId) {
        User user = userRetriever.findById(userId);
        Food food = foodRetriever.findById(foodId);

        validateFoodOwner(user.getId(), food);

        return food.getFresh();
    }

    @Transactional
    public String updateFreshnessJson(
            final Long userId,
            final Long foodId,
            final FoodFreshRequestDto foodFreshRequestDto
    ) {
        User user = userRetriever.findById(userId);
        Food food = foodRetriever.findById(foodId);

        validateFoodOwner(user.getId(), food);

        String imageUrl = null;
        try {
            imageUrl = s3Service.uploadImage(FOOD_S3_UPLOAD_FOLDER, foodFreshRequestDto.image());
        } catch (BusinessException e) {
            throw new BusinessException(e.getErrorCode());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        String result = openAiService.analyzeFreshness(food.getName(), imageUrl);

        foodEditor.updateFreshness(food, result);

        return result;
    }

    @Transactional
    public String extractFoodImage(
            final Long userId,
            final FoodImageDto foodImageDto
    ) {

        String imageUrl = null;
        try {
            imageUrl = s3Service.uploadImage(FOOD_S3_UPLOAD_FOLDER, foodImageDto.image());
        } catch (BusinessException e) {
            throw new BusinessException(e.getErrorCode());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return openAiService.ExtractNameAndExpiration(imageUrl);
    }
}
