package com.daelim.communitybackend.repository;

import com.daelim.communitybackend.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByBoardId(Pageable pageable, Integer boardId);
    Page<Post> findAllByTitleLikeIgnoreCaseOrContentLikeIgnoreCaseOrUserIdLike(Pageable pageable, String title, String content, String userId);

    Page<Post> findAllByOrderByRecommendDesc(Pageable pageable);
    Page<Post> findAllByBoardIdOrderByRecommendDesc(Pageable pageable, Integer boardId);
    Page<Post> findAllByUserId(Pageable pageable, String userId);
}
