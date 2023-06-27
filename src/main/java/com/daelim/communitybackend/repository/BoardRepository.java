package com.daelim.communitybackend.repository;

import com.daelim.communitybackend.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    @Query(value = "SELECT b.boardId, b.boardName, COUNT(p.postId) AS count\n" +
            "FROM Board b\n" +
            "LEFT JOIN Post p ON b.boardId = p.boardId\n" +
            "GROUP BY b.boardId, b.boardName\n" +
            "ORDER BY COUNT(p.postId) DESC limit 8;"
            , nativeQuery = true)
    List<Object> getBoardsByLank();

    Optional<Board> findBoardByBoardName(String boardName);
    Page<Board> findAllByBoardNameLikeIgnoreCaseOrUserIdLike(Pageable pageable, String boardName, String userId);
}
