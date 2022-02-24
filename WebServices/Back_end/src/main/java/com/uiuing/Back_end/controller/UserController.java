/*
 * @Author        : uiuing
 * @Date          : 2021-06-12 19:04:41
 * @LastEditTime  : 2021-06-29 16:19:40
 * @LastEditors   : uiuing
 * @Description   : 
 * @FilePath      : /Back_end/src/main/java/com/uiuing/Back_end/controller/UserController.java
 * ©️ uiuing.com
 */
package com.uiuing.Back_end.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.uiuing.Back_end.dao.RedisDao;
import com.uiuing.Back_end.dao.userDao;
import com.uiuing.Back_end.entity.direction;
import com.uiuing.Back_end.entity.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Resource
    userDao userDao;

    @Autowired
    private RedisDao redisUtil;

    /**
     * API
     * 
     * @description : 新增用户
     * @param {*} 用户邮箱 用户名 用户密码
     * @return {*}
     */
    @CrossOrigin
    @RequestMapping(value = "/inster", method = RequestMethod.POST)
    @ResponseBody
    public void user_inster(@RequestBody user user) {
        String times = String.valueOf(new Date().getTime() / 1000 / 60 / 60 / 24 * 24 * 60 * 60);
        JSONObject obj = new JSONObject();
        obj.put(times, 0);
        redisUtil.set(user.getEmail() + "_HeatMap", JSONObject.toJSONString(obj));
        userDao.insert_user(user.getName(), user.getEmail(), user.getPassword());
    }

    /**
     * API
     * 
     * @description : 判断用户邮箱是否存在
     * @param {*} 用户邮箱
     * @return {*} boolean
     */
    @CrossOrigin
    @RequestMapping(value = "/select/email", method = RequestMethod.POST)
    @ResponseBody
    public boolean user_select_email(@RequestBody user email) {
        if (userDao.select_email(email.getEmail()).size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * API
     * 
     * @description : 判断用户名是否存在
     * @param {*} 用户名
     * @return {*} boolean
     */
    @CrossOrigin
    @RequestMapping(value = "/select/name", method = RequestMethod.POST)
    @ResponseBody
    public boolean user_select_name(@RequestBody user name) {
        if (userDao.select_name(name.getName()).size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * API
     * 
     * @description : 查询用户所有数据
     * @param {*} 用户邮箱
     * @return {*} 用户所有信息
     */
    @CrossOrigin
    @RequestMapping(value = "/select/all", method = RequestMethod.POST)
    @ResponseBody
    public List<user> user_select_all(@RequestBody user email) {
        List<user> users = userDao.select_all(email.getEmail());
        if (users.size() == 0) {
            return null;
        }
        return users;
    }

    /**
     * API
     * 
     * @description : 判断用户密码是否正确
     * @param {*} 用户邮箱 用户密码
     * @return {*} boolean
     */
    @CrossOrigin
    @RequestMapping(value = "/select/password", method = RequestMethod.POST)
    @ResponseBody
    public boolean user_select_password(@RequestBody user user) {
        if (userDao.select_password(user.getEmail(), user.getPassword()).size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * API
     * 
     * @description : 获取用户网易云音乐歌单ID
     * @param {*} 用户邮箱
     * @return {*} String
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/get/music", method = RequestMethod.POST)
    public String get_music(@RequestBody user email) {
        return redisUtil.get(email.getEmail() + "_MusicId");
    }

    /**
     * API
     * 
     * @description : 设置用户网易云音乐歌单ID
     * @param {*} 用户邮箱 用户音乐id
     * @return {*}
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/set/music", method = RequestMethod.POST)
    public void set_music(@RequestBody user user) {
        redisUtil.set(user.getEmail() + "_MusicId", user.getMusicid());
    }

    /**
     * API
     * 
     * @description : 获取用户热力图数据
     * @param {*}
     * @return {*} JSONObject
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/get/value", method = RequestMethod.POST)
    public JSONObject get_value(@RequestBody user email) {
        // Convert Json String To JsonArray
        return JSONObject.parseObject(redisUtil.get(email.getEmail() + "_HeatMap"));
    }

    /**
     * API
     * 
     * @description : 添加用户热力图数据
     * @param {*}
     * @return {*}
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/set/value", method = RequestMethod.POST)
    public void set_value(@RequestBody user email) {
        String times = String.valueOf(new Date().getTime() / 1000 / 60 / 60 / 24 * 24 * 60 * 60);
        String str = redisUtil.get(email.getEmail() + "_HeatMap");
        JSONObject obj = new JSONObject();
        // 用户数据存在,保存记录
        if (str != null && str != "" && str != " ") {
            obj = JSONObject.parseObject(str);
            // 用户数据不存在，初始化
        }
        // 时间节点存在，更新记录
        if (obj != null && obj.containsKey(times)) {
            obj.replace(times, (int) obj.get(times) + 1);
            // 时间节点不存在，初始化
        } else {
            obj.put(times, 1);
        }
        redisUtil.set(email.getEmail() + "_HeatMap", JSONObject.toJSONString(obj));
    }

    /**
     * API
     * 
     * @description : 获取用户领域数据
     * @param {*}
     * @return
     * @return {*} JSONObject
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/get/direction", method = RequestMethod.POST)
    public List<direction> get_direction(@RequestBody user email) {
        return userDao.select_direction(email.getEmail());
    }

    /**
     * API
     * 
     * @description : 获取用户领域详情数据
     * @param {*}
     * @return
     * @return {*} JSONObject
     */
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/get/direction_key", method = RequestMethod.POST)
    public Object get_direction_key(@RequestBody user email) {
        return JSONObject.parse(redisUtil.get(email.getEmail() + "_KeyWords"));
    }
}