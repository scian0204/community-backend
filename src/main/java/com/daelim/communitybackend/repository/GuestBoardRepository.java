package com.daelim.communitybackend.repository;

import com.daelim.communitybackend.entity.GuestBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestBoardRepository extends JpaRepository<GuestBoard, Integer> {
    Page<GuestBoard> findAllByTarget(Pageable pageable, String userId);

}
