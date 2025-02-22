package org.wefresh.wefresh_server.external.service.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.wefresh.wefresh_server.external.service.google.dto.GoogleTokenDto;

@FeignClient(name = "googleTokenClient", url = "https://oauth2.googleapis.com/token")
public interface GoogleTokenClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenDto getToken(@RequestBody MultiValueMap<String, String> requestBody);
}
