/*
 * @Author        : uiuing
 * @Date          : 2021-05-17 21:49:07
 * @LastEditTime  : 2021-06-28 19:29:44
 * @LastEditors   : uiuing
 * @Description   : 查询接口
 * @FilePath      : /Back_end/src/main/java/com/uiuing/Back_end/dao/userDao.java
 * ©️ uiuing.com
 */

package com.uiuing.Back_end.dao;

import com.uiuing.Back_end.entity.direction;
import com.uiuing.Back_end.entity.user;

import java.util.List;

public interface userDao {
    /**
     * @description : 查询email数据
     * @param {String} email
     * @return {*} List<user>
     */
    public List<user> select_email(String email);

       /**
     * @description : 查询name数据
     * @param {String} name
     * @return {*} List<user>
     */
    public List<user> select_name(String name);

           /**
     * @description : 查询user数据
     * @param {String} name
     * @return {*} List<user>
     */
    public List<user> select_all(String email);

       /**
     * @description : 查询password数据
     * @param {String} name
     * @return {*} List<user>
     */
    public List<user> select_password(String email,String password);

    /**
     * @description  :  新增用户
     * @param         {String} name
     * @param         {String} email
     * @param         {String} password
     * @param         {String} num
     * @return        {*}
     */
    public void insert_user(String name, String email,String password);

     /**
     * @description : 查询用户领域数据
     * @param {String} email
     * @return {*}
     */
    public List<direction> select_direction(String email);

    /**
     * @description : 查询用户领域数据
     * @param {String} email
     * @return {*}
     */
    public List<direction> update_direction(String email,String obj);
    
}