package com.start.entitle;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @TableName user
 */

@Data
public class User {
    private Long userId;//

    private String account;//唯一

    private String password;

    private String avaterUrl;//头像url 需默认
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;//注册时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;//登录时间

    private String bio;//简介 需默认

    private Integer isAdmin;//是否为管理员 y

    private String nickname;//昵称

    private Integer isDeleted;//是否注销 y

    private Integer isBanned;//是否封禁 y
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date banStartTime;//封禁起始时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date banEndTime;//封禁结束时间

    private Long vipGrade;//vip等级
}