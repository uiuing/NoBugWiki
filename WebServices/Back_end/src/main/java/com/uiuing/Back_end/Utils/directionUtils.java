/*
 * @Author        : uiuing
 * @Date          : 2021-06-28 13:20:22
 * @LastEditTime  : 2021-06-29 15:21:43
 * @LastEditors   : uiuing
 * @Description   :  获取direction.json 领域数据
 * @FilePath      : /Back_end/src/main/java/com/uiuIng/Back_end/Utils/directionUtils.java
 * ©️ uiuIng.com
 */
package com.uiuing.Back_end.Utils;

import java.io.File;
import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.io.FileUtils;

public class directionUtils {

    public static JSONArray getDirectionJson() {
        File file = new File("src/main/java/com/uiuIng/Back_end/data/direction.json");
        String file1;
        try {
            file1 = FileUtils.readFileToString(file);
            JSONArray obj = JSON.parseArray(file1);
            return obj;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
