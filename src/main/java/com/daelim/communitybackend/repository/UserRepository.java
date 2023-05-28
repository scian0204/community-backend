package com.daelim.communitybackend.repository;

import com.daelim.communitybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u.isAdmin from User AS u where u.userId= :userId")
    Boolean findIsAdminByUserId(String userId);
}
