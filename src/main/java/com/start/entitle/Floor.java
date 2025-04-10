package com.start.entitle;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName floor
 */
@Data
public class Floor {
    private Long floorId;

    private Long postId;

    private Long userId;

    private Long floorNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Long likeCount;

    private Integer isDeleted;

    private String content;
}