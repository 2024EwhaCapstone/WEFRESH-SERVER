package org.wefresh.wefresh_server.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wefresh.wefresh_server.bookmark.service.BookmarkService;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.dto.ResponseDto;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/recipes/{id}")
    public ResponseEntity<ResponseDto<Void>> createBookmark(
            @UserId final Long userId,
            @PathVariable final Long id,
            @RequestParam(required = false) final String type
    ) {
        bookmarkService.createBookmark(userId, id, type);
        return ResponseEntity.ok().body(ResponseDto.success());
    }
}
