package online.mengxun.server.response;

import com.alibaba.fastjson.JSONObject;

public class Check {
    //非空校验，判断一个字符串是否为空，是的话则返回true
    public boolean emptyStr(String str) {
        if (str == "" || str == null) {
            return true;
        } else {
            return false;
        }
    }

    //键值存在校验
    public boolean noKey(JSONObject jsonObject, String key) {
        if (!jsonObject.containsKey(key)) {
            return true;
        } else {
            return false;
        }
    }
}
