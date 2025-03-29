package com.start.entitle;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName board
 */
@Data
public class Board {
    private Long boardId;

    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Long viewCount;

    private Long postCount;
}