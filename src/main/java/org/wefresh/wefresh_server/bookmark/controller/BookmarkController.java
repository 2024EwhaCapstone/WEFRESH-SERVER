package org.wefresh.wefresh_server.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wefresh.wefresh_server.bookmark.dto.response.BookmarkListsDto;
import org.wefresh.wefresh_server.bookmark.dto.response.BookmarkListsPageDto;
import org.wefresh.wefresh_server.bookmark.service.BookmarkService;
import org.wefresh.wefresh_server.common.auth.annotation.UserId;
import org.wefresh.wefresh_server.common.dto.ResponseDto;
import org.wefresh.wefresh_server.recipe.dto.response.RecipeDto;

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

    @GetMapping("/bookmarks")
    public ResponseEntity<ResponseDto<BookmarkListsPageDto>> getBookmarks(
            @UserId final Long userId,
            @PageableDefault(page = 0, size = 15) Pageable pageable
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(bookmarkService.getBookmarks(userId, pageable)));
    }

    @GetMapping("/bookmarks/six")
    public ResponseEntity<ResponseDto<BookmarkListsDto>> getSixBookmarks(
            @UserId final Long userId
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(bookmarkService.getSixBookmarks(userId)));
    }

    @GetMapping("/bookmarks/{bookmarkId}")
    public ResponseEntity<ResponseDto<RecipeDto>> getBookmark(
            @UserId final Long userId,
            @PathVariable final Long bookmarkId
    ) {
        return ResponseEntity.ok().body(ResponseDto.success(bookmarkService.getBookmark(userId, bookmarkId)));
    }

    @DeleteMapping("/bookmarks/{bookmarkId}")
    public ResponseEntity<ResponseDto<Void>> deleteBookmark(
            @UserId final Long userId,
            @PathVariable final Long bookmarkId
    ) {
        bookmarkService.deleteBookmark(userId, bookmarkId);
        return ResponseEntity.ok().body(ResponseDto.success());
    }
}
