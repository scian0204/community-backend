package com.daelim.communitybackend.repository;

import com.daelim.communitybackend.entity.RecmdUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecmdUserRepository extends JpaRepository<RecmdUser, Integer> {
    Optional<RecmdUser> findByUserIdAndPostId(String userId, Integer postId);
}
