package org.wefresh.wefresh_server.bookmark.dto.response;

import org.springframework.data.domain.Page;
import org.wefresh.wefresh_server.bookmark.domain.Bookmark;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.RecipeErrorCode;

import java.util.List;

public record BookmarkListsDto(
        List<BookmarkListDto> bookmarks,
        PaginationDto pagination
) {
    public static BookmarkListsDto from (Page<Bookmark> bookmarks) {
        return new BookmarkListsDto(
                bookmarks.stream()
                        .map(BookmarkListDto::from)
                        .toList(),
                new PaginationDto(
                        bookmarks.getNumber(),
                        bookmarks.getSize(),
                        bookmarks.getTotalPages(),
                        bookmarks.getTotalElements(),
                        bookmarks.isLast()
                )
        );
    }

    public record BookmarkListDto(
            Long bookmarkId,
            String name,
            String image
    ) {
        public static BookmarkListDto from(Bookmark bookmark) {
            if (bookmark.getRecipe() != null) {
                return new BookmarkListDto(
                        bookmark.getId(),
                        bookmark.getRecipe().getName(),
                        bookmark.getRecipe().getImage()
                );
            } else if (bookmark.getTodayRecipe() != null) {
                return new BookmarkListDto(
                        bookmark.getId(),
                        bookmark.getTodayRecipe().getName(),
                        bookmark.getTodayRecipe().getImage()
                );
            } else {
                throw new BusinessException(RecipeErrorCode.RECIPE_NOT_FOUND);
            }
        }
    }

    public record PaginationDto(
            int currentPage,
            int pageSize,
            int totalPages,
            long totalElements,
            boolean isLastPage
    ) {}
}
