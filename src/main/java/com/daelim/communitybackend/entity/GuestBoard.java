package com.daelim.communitybackend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
public class GuestBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gBoardId;
    private String userId;
    private String content;
    private Timestamp writeDate;
    private String target;
}
