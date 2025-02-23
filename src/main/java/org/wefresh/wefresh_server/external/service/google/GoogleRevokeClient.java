package org.wefresh.wefresh_server.external.service.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "googleRevokeClient", url = "https://oauth2.googleapis.com")
public interface GoogleRevokeClient {

    @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void revokeToken(@RequestBody String body);
}


