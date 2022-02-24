/*
 * @Author        : uiuing
 * @Date          : 2021-05-18 14:03:07
 * @LastEditTime  : 2021-06-29 15:06:30
 * @LastEditors   : uiuing
 * @Description   : 数据控制
 * @FilePath      : /Back_end/src/main/java/com/uiuing/Back_end/controller/SearchInfoController.java
 * ©️ uiuing.com
 */

package com.uiuing.Back_end.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.uiuing.Back_end.dao.RedisDao;
import com.uiuing.Back_end.entity.user;
import com.uiuing.Back_end.computing.FieldComputing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class SearchInfoController {

    @Autowired
    private RedisDao redisUtil;

    @Autowired
    private KafkaTemplate template;

    /**
     * @description : 返回查询结果
     * @param {*}
     * @return {*}
     */
    @CrossOrigin
    @RequestMapping(value = "/Search/Inject", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray Search_Inject(@RequestBody user Key) {

        // 获取输入值
        String KeyWord = Key.getKeyword();
        String email = Key.getEmail();

        // 保存用户搜索行为
        SaveQueryBehavior(email, KeyWord);

        // 返回查询结果
        return QueryResultCalculation(KeyWord, email);
    }

    /**
     * @description : 热门内容推荐
     * @param {*}
     * @return {*}
     */
    @CrossOrigin
    @RequestMapping(value = "/Search/Hot", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray Search_Hot(@RequestBody user Email) {

        // 获取用户识别
        String email = Email.getEmail();

        // 获取个性化内容
        String hot = redisUtil.get(email + "_HotContent");
        // 判断用户个性化推荐内容是否存在
        if (hot == null || hot.length() == 0) {
            // 不存在则选用初始化内容
            hot = redisUtil.get("HotContent");
        }
        // 返回查询结果
        return JSONArray.parseArray(hot);
    }

    /**
     * @description : 用户存活情况
     * @param {*}
     * @return {*}
     */
    @CrossOrigin
    @RequestMapping(value = "/Search/Activation", method = RequestMethod.POST)
    @ResponseBody
    public void Search_Activation_user(@RequestBody user Email) {
        // 获取用户识别
        String email = Email.getEmail();
        // 激活用户状态
        String times = String.valueOf(new Date().getTime() / 1000 / 60); // 分钟
        JSONObject obj = JSONObject.parseObject(redisUtil.get("SurvivingUsers"));
        // 用户存在，更新记录
        if (obj != null && obj.containsKey(email)) {
            obj.replace(email, times);
            // 用户不存在,插入
        } else {
            obj.put(email, times);
        }
        redisUtil.set("SurvivingUsers", JSONObject.toJSONString(obj));
    }

    /**
     * @description : 保存用户搜索行为
     * @param {String} email
     * @param {String} KeyWord
     * @return {*}
     */
    private void SaveQueryBehavior(String email, String KeyWord) {
        // 保存用户查询行为
        // 日期计算
        String SearchKey = email + "_SearchRecords";
        String times = String.valueOf(new Date().getTime() / 1000 / 60 / 60 / 24 * 24 * 60 * 60);
        String str = redisUtil.get(SearchKey);
        JSONObject obj = new JSONObject();
        // 用户数据存在,保存记录
        if (str != null && str != "" && str != " ") {
            obj = JSONObject.parseObject(str);
            // 用户数据不存在，初始化
        }
        JSONArray obj_arr = (JSONArray) obj.get(times);
        // 时间节点存在，更新记录
        if (obj != null && obj_arr != null) {
            obj_arr.add(KeyWord);
            obj.replace(times, obj_arr.toArray());
            // 时间节点不存在，初始化
        } else {
            JSONArray obj_ram = new JSONArray();
            obj_ram.add(KeyWord);
            obj.put(times, obj_ram.toArray());
        }
        redisUtil.set(SearchKey, JSONObject.toJSONString(obj));
    }

    /**
     * @description : 查询结果计算
     * @param {String} KeyWord
     * @param {String} email
     * @return {*}
     */
    private JSONArray QueryResultCalculation(String KeyWord, String email) {
        // 判断是否已经存在
        String KeyWord_Value = redisUtil.get(KeyWord);
        if (KeyWord_Value != null) {
            JSONArray res = JSON.parseArray(KeyWord_Value);
            // 开启领域计算
            DomainComputing(email, res);
            return res;

        }
        // 生产工作ID
        String JobId = String.valueOf((int) ((Math.random()) * 10000000));
        redisUtil.set(JobId, KeyWord);
        // 分配工作
        template.send("Search_Inject_Request", JobId);

        // 等待工作完成
        while (true) {
            String value = redisUtil.get(JobId);
            if (value != null && value.length() >= 38) {
                JSONArray res = SortAndFindKeyWords(value);
                redisUtil.set(KeyWord, res.toString());
                redisUtil.expire(KeyWord, 1, TimeUnit.DAYS);
                // 开启领域计算
                DomainComputing(email, res);
                return res;
            } else {
                try {
                    Thread.currentThread();
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @description : 对用户搜索结果进行领域计算
     * @param {String}    email
     * @param {JSONArray} res
     * @return {*}
     */
    private void DomainComputing(String email, JSONArray res) {
        // 开启领域计算
        Thread t = new Thread(new FieldComputing(email, res, redisUtil));
        t.start(); // 启动新线程

    }

    /**
     * @description : 对结果进行去重和内容排序
     * @param {String} jsonArrStr
     * @return {*}
     */
    private JSONArray SortAndFindKeyWords(String jsonArrStr) {
        // Convert Json String To JsonArray
        JSONArray jsonArr = JSON.parseArray(jsonArrStr);
        // Storing The Sort Result Json Array
        JSONArray sortedJsonArray = new JSONArray();
        // List For Sorting
        List<JSONObject> list = new ArrayList<JSONObject>();
        // Traverse The Json Array To Be Sorted And Put The Data Into The List
        for (int i = 0; i < jsonArr.size(); i++) {
            list.add(jsonArr.getJSONObject(i));
        }
        Collections.sort(list, new Comparator<JSONObject>() {
            // Sort Field
            private static final String KEY_NAME = "frequency";

            // Rewrite Compare Sort
            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();
                try {
                    valA = a.getString(KEY_NAME);
                    valB = b.getString(KEY_NAME);
                    // 寻找关键词找到官方网站
                } catch (JSONException e) {
                    System.out.println(e);
                }
                // Set Collation
                int i = valB.compareTo(valA);
                return i;
            }
        });

        int name_index = 1;

        List<JSONObject> list2 = new ArrayList<JSONObject>();
        // Put The Sorted Results Into The Result JsonArray And Remove The Duplication
        for (int i = 0; i < list.size(); i++) {
            boolean found = true;
            for (int j = 0; j < list2.size(); j++) {
                if (list.get(i).getString("introduction").equals(list2.get(j).getString("introduction"))) {
                    found = false;
                }
            }
            if (found) {
                list2.add(list.get(i));
                JSONObject obj = list.get(i);
                obj.remove("introduction");
                obj.put("name", name_index++);
                sortedJsonArray.add(obj);
            }
        }
        return sortedJsonArray;
    }
}
