package org.wefresh.wefresh_server.food.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.FoodErrorCode;
import org.wefresh.wefresh_server.external.service.s3.S3Service;
import org.wefresh.wefresh_server.food.domain.Category;
import org.wefresh.wefresh_server.food.domain.Food;
import org.wefresh.wefresh_server.food.dto.request.FoodRegisterDto;
import org.wefresh.wefresh_server.food.dto.response.FoodDto;
import org.wefresh.wefresh_server.food.dto.response.FoodListsDto;
import org.wefresh.wefresh_server.food.manager.FoodRetriever;
import org.wefresh.wefresh_server.food.manager.FoodSaver;
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

    static final String FOOD_S3_UPLOAD_FOLDER = "foods/";

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

        applicationContext.getBean(FoodService.class).saveFood(user, foodRegisterDto, imageUrl);
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
    protected void saveFood(User user, FoodRegisterDto foodRegisterDto, String imageUrl) {
        try {
            Food food = Food.builder()
                    .name(foodRegisterDto.name())
                    .image(imageUrl)
                    .category(foodRegisterDto.getCategoryEnum())
                    .date(foodRegisterDto.date())
                    .count(foodRegisterDto.count())
                    .memo(foodRegisterDto.memo())
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
}
