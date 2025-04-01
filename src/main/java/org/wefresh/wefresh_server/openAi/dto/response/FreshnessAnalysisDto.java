package org.wefresh.wefresh_server.openAi.dto.response;

import java.util.List;

public record FreshnessAnalysisDto(
        String freshness,
        List<Reason> reasons
) {
    public record Reason(
            String summary,
            String description
    ) {}
}

