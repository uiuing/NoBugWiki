/*
 * @Author        : uiuIng
 * @Date          : 2021-06-28 19:53:57
 * @LastEditTime  : 2021-06-29 15:22:23
 * @LastEditors   : uiuIng
 * @Description   : 领域计算
 * @FilePath      : /Back_end/src/main/java/com/uiuIng/Back_end/computing/FieldComputing.java
 * ©️ uiuIng.com
 */
package com.uiuing.Back_end.computing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.uiuing.Back_end.dao.RedisDao;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.uiuing.Back_end.Utils.DBUtil;
import com.uiuing.Back_end.Utils.directionUtils;

public class FieldComputing implements Runnable {

    // 区分领域

    private String email;
    private JSONArray value;
    private String str;
    private Matcher matcher;
    private int max_num;
    private String max_key;
    private String Keyword_Detail;
    private RedisDao redisUtil;

    public FieldComputing(String email, JSONArray value, RedisDao redisUtil) {
        this.email = email;
        this.value = value;
        this.redisUtil = redisUtil;
    }

    public void run() {

        // 获取文章内容
        for (int i = 0; i < value.size(); i++) {
            JSONObject obj = value.getJSONObject(i);
            str += obj.getString("title") + obj.getString("introduction");
        }
        // 开始进行此次查询领域划分
        // Get Json
        JSONArray obj_array = directionUtils.getDirectionJson();
        for (int i = 0; i < obj_array.size(); i++) {
            JSONObject obj = (JSONObject) obj_array.get(i);
            // Get Key
            Map<String, Object> map = obj;
            for (Entry<String, Object> entry : map.entrySet()) {
                String Key = entry.getKey();
                // Get Value
                JSONArray obj_ram = (JSONArray) obj.get(Key);
                for (int j = 0; j < obj_ram.size(); j++) {
                    // 关键字详情
                    String Keyword_Details = (String) obj_ram.get(j);
                    // 正则提取关键词次数
                    matcher = Pattern.compile("(?i)" + Keyword_Details).matcher(str);
                    int count = 0;
                    while (matcher.find())
                        count++;
                    if (count > max_num) {
                        max_num = count;
                        max_key = Key;
                        Keyword_Detail = Keyword_Details;
                    }
                }
            }
        }
        // 存储关键词
        String str = redisUtil.get(email + "_KeyWords");
        JSONObject obj = new JSONObject();
        // 用户数据存在,保存记录
        if (str != null && str != "" && str != " ") {
            // 用户数据不存在，初始化
            obj = JSONObject.parseObject(str);
        }
        // 关键字节点存在，更新记录
        if (obj != null && obj.containsKey(Keyword_Detail)) {
            obj.replace(Keyword_Detail, (int) obj.get(Keyword_Detail) + 1);
            // 时间节点不存在，初始化
        } else {
            obj.put(Keyword_Detail, 1);
        }
        redisUtil.set(email + "_KeyWords", JSONObject.toJSONString(obj));

        // 领域存储数据
        String SQL_UPDATE = "update direction set " + max_key + "=" + max_key + "+1 where email = \"" + email + "\"";
        Connection conn = DBUtil.getConn();
        PreparedStatement state = null;
        ResultSet resultSte = null;
        try {
            state = conn.prepareStatement(SQL_UPDATE);
            state.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(conn, state, resultSte);
        }
    }
}
