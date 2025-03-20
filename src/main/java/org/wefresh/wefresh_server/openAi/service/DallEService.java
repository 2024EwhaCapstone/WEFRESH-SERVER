package org.wefresh.wefresh_server.openAi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.wefresh.wefresh_server.external.service.s3.S3Service;
import org.wefresh.wefresh_server.openAi.dto.request.DalleRequestDto;
import org.wefresh.wefresh_server.openAi.dto.request.GptRequestDto;
import org.wefresh.wefresh_server.openAi.dto.response.dalle.DalleResponseDto;
import org.wefresh.wefresh_server.openAi.dto.response.gpt.GptResponseDto;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DallEService {
    @Value("${dalle.api-url}")
    private String dalleApiUrl;

    @Value("${openai.api-url}")
    private String apiURL;

    private final RestTemplate restTemplate;
    private final S3Service s3Service; // S3 업로드 서비스 추가

    public String generateAndUploadImage(String recipeName) {
        String translatedPrompt = translateToEnglish(recipeName);
        System.out.println("translatedPrompt = " + translatedPrompt);

        // DALL·E에 요청
        DalleRequestDto request = DalleRequestDto.of(translatedPrompt);
        DalleResponseDto response = restTemplate.postForObject(dalleApiUrl, request, DalleResponseDto.class);

        if (response == null || response.data() == null || response.data().isEmpty()) {
            return "defaultImageURL";  // DALL·E 이미지 생성 실패 시 기본 이미지 반환
        }

        String dallEImageUrl = response.data().get(0).url(); // DALL·E 이미지 URL
        return uploadToS3(dallEImageUrl, recipeName); // S3 업로드 후 URL 반환
    }

    private String uploadToS3(String imageUrl, String recipeName) {
        try {
            return s3Service.uploadImageFromUrl("recipes/", imageUrl, recipeName);
        } catch (IOException e) {
            e.printStackTrace();
            return "defaultImageURL"; // 업로드 실패 시 기본 이미지 반환
        }
    }

    public String translateToEnglish(String koreanPrompt) {
        String gptPrompt = "Translate the following food name to English. Return only the translated food name without any additional text: " + koreanPrompt;
        GptRequestDto request = GptRequestDto.of("gpt-4o-mini", gptPrompt);
        GptResponseDto response = restTemplate.postForObject(apiURL, request, GptResponseDto.class);
        return response.choices().get(0).message().content(); // 영어 번역 결과 반환
    }
}


