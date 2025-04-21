package com.start.entitle;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName post
 */
@Data
public class Post {
    private Long postId;//

    private Long boardId;

    private Long userId;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    private Long viewCount;

    private Long likeCount;

    private Integer isEssence;//是否是精华贴

    private Integer isDeleted;

    private Date sortTime;

    private Long floorNumber;//楼层

    private String content;//内容

}