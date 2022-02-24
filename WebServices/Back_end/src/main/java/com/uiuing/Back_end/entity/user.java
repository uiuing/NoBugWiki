/*
 * @Author        : uiuing
 * @Date          : 2021-05-17 21:35:13
 * @LastEditTime  : 2021-06-29 15:15:37
 * @LastEditors   : uiuing
 * @Description   :  user 字段映射
 * @FilePath      : /Back_end/src/main/java/com/uiuing/Back_end/entity/user.java
 * ©️ uiuing.com
 */

package com.uiuing.Back_end.entity;

public class user {

    private String name;
    private String email;
    private String password;
    private String keyword;
    private String musicid;

    public String getMusicid() {
        return musicid;
    }

    public void setMusicid(String musicid) {
        this.musicid = musicid;
    }

    public String getName() {
        return name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}