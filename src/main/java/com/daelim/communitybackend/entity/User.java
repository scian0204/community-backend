package com.daelim.communitybackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity
@Data
@DynamicUpdate
@DynamicInsert
public class User {
    @Id
    private String userId;
    private String userName;
    private String password;
    private String image;
    private Timestamp regDate;
    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isAdmin;
}
