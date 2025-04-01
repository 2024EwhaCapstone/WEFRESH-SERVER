package org.wefresh.wefresh_server.openAi.dto.response.dalle;

import java.util.List;

public record DalleResponseDto(
        List<DataDto> data
) {
    public record DataDto(
            String url // 생성된 이미지 URL
    ) {
    }
}

