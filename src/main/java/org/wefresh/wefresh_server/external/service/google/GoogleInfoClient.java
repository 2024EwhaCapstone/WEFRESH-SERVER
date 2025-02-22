package org.wefresh.wefresh_server.external.service.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.wefresh.wefresh_server.external.service.google.dto.GoogleuserDto;

@FeignClient(name="googleInfoClient", url = "https://www.googleapis.com/oauth2/v3/userinfo")
public interface GoogleInfoClient {
    @GetMapping
    GoogleuserDto getUserInformation(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
    );

}
