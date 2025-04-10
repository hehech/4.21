package com.start.entitle;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName comment
 */
@Data
public class Comment {
    private Long commentId;

    private Long floorId;

    private Long userId;

    private Long parentCommentId;

    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer likeCount;

    private Integer isDeleted;
}