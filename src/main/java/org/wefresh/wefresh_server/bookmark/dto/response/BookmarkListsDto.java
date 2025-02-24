package org.wefresh.wefresh_server.bookmark.dto.response;

import org.wefresh.wefresh_server.bookmark.domain.Bookmark;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.RecipeErrorCode;

import java.util.List;

public record BookmarkListsDto(
        List<BookmarkListDto> bookmarks
) {
    public static BookmarkListsDto from (List<Bookmark> bookmarks) {
        return new BookmarkListsDto(
                bookmarks.stream()
                        .map(BookmarkListDto::from)
                        .toList()
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
}
