package com.daelim.communitybackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Timestamp;

@Entity
@Data
@DynamicInsert
@DynamicUpdate
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boardId;
    private String boardName;
    private String userId;
    private Timestamp regDate;
    @Column(columnDefinition = "TINYINT(1)")
    private Boolean isAllowed;
}
