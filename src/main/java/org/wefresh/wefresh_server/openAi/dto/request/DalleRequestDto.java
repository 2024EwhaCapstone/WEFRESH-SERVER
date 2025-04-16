package org.wefresh.wefresh_server.openAi.dto.request;

public record DalleRequestDto(
        String model,
        String prompt,
        int n, // 생성할 이미지 개수
        String size // 이미지 크기 (예: "1024x1024")
) {
    public static DalleRequestDto of(String prompt) {
        return new DalleRequestDto(
                "dall-e-2",
                prompt,
                1, // 한 개의 이미지 생성
                "512x512"
        );
    }
}

