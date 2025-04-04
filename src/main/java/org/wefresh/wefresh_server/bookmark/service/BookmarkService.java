package org.wefresh.wefresh_server.bookmark.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wefresh.wefresh_server.bookmark.domain.Bookmark;
import org.wefresh.wefresh_server.bookmark.dto.response.BookmarkListsDto;
import org.wefresh.wefresh_server.bookmark.dto.response.BookmarkListsPageDto;
import org.wefresh.wefresh_server.bookmark.manager.BookmarkCreator;
import org.wefresh.wefresh_server.bookmark.manager.BookmarkRemover;
import org.wefresh.wefresh_server.bookmark.manager.BookmarkRetriever;
import org.wefresh.wefresh_server.common.exception.BusinessException;
import org.wefresh.wefresh_server.common.exception.code.BookmarkErrorCode;
import org.wefresh.wefresh_server.common.exception.code.RecipeErrorCode;
import org.wefresh.wefresh_server.recipe.domain.Recipe;
import org.wefresh.wefresh_server.recipe.domain.RecipeBase;
import org.wefresh.wefresh_server.recipe.dto.response.RecipeDto;
import org.wefresh.wefresh_server.recipe.manager.RecipeRetriever;
import org.wefresh.wefresh_server.todayRecipe.domain.TodayRecipe;
import org.wefresh.wefresh_server.todayRecipe.manager.TodayRecipeRetriever;
import org.wefresh.wefresh_server.user.domain.User;
import org.wefresh.wefresh_server.user.manager.UserRetriever;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkCreator bookmarkCreator;
    private final UserRetriever userRetriever;
    private final RecipeRetriever recipeRetriever;
    private final TodayRecipeRetriever todayRecipeRetriever;
    private final BookmarkRetriever bookmarkRetriever;
    private final BookmarkRemover bookmarkRemover;

    @Transactional
    public void createBookmark(
            final Long userId,
            final Long id,
            final String type
    ) {
        User user = userRetriever.findById(userId);

        Bookmark bookmark = null;
        if (type.equals("general")) {
            Recipe recipe = recipeRetriever.findById(id);
            bookmark = buildBookmark(recipe, user);
        } else if (type.equals("today")) {
            TodayRecipe todayRecipe = todayRecipeRetriever.findById(id);
            bookmark = buildBookmark(todayRecipe, user);
        } else {
            throw new BusinessException(RecipeErrorCode.RECIPE_NOT_FOUND);
        }

        bookmarkCreator.save(bookmark);
    }

    @Transactional(readOnly = true)
    public BookmarkListsPageDto getBookmarks(
            final Long userId,
            Pageable pageable
    ) {
        User user = userRetriever.findById(userId);

        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Bookmark> bookmarks = bookmarkRetriever.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable);

        return BookmarkListsPageDto.from(bookmarks);
    }

    @Transactional(readOnly = true)
    public BookmarkListsDto getSixBookmarks(final Long userId) {
        User user = userRetriever.findById(userId);

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "createdAt"));
        return BookmarkListsDto.from(bookmarkRetriever.findByUserId(user.getId(), pageable));
    }

    @Transactional(readOnly = true)
    public RecipeDto getBookmark(
            final Long userId,
            final Long bookmarkId
    ) {
        User user = userRetriever.findById(userId);
        Bookmark bookmark = bookmarkRetriever.findById(bookmarkId);

        validateBookmarkOwner(user.getId(), bookmark);

        if (bookmark.getRecipe() != null) {
            return RecipeDto.from(bookmark.getRecipe());
        } else if (bookmark.getTodayRecipe() != null) {
            return RecipeDto.from(bookmark.getTodayRecipe());
        } else {
            throw new BusinessException(RecipeErrorCode.RECIPE_NOT_FOUND);
        }
    }

    @Transactional
    public void deleteBookmark(
            final Long userId,
            final Long bookmarkId
    ) {
        User user = userRetriever.findById(userId);
        Bookmark bookmark = bookmarkRetriever.findById(bookmarkId);
        validateBookmarkOwner(user.getId(), bookmark);

        bookmarkRemover.deleteById(bookmark.getId());
    }

    private Bookmark buildBookmark(RecipeBase recipe, User user) {
        if (recipe instanceof Recipe generalRecipe) {
            return Bookmark.builder()
                    .recipe(generalRecipe)
                    .user(user)
                    .build();
        } else if (recipe instanceof TodayRecipe todayRecipe) {
            return Bookmark.builder()
                    .todayRecipe(todayRecipe)
                    .user(user)
                    .build();
        } else {
            throw new BusinessException(RecipeErrorCode.RECIPE_NOT_FOUND);
        }
    }

    private void validateBookmarkOwner(Long userId, Bookmark bookmark) {
        if (!bookmark.getUser().getId().equals(userId)) {
            throw new BusinessException(BookmarkErrorCode.BOOKMARK_FORBIDDEN);
        }
    }
}
