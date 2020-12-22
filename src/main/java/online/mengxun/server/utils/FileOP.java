package online.mengxun.server.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class FileOP {

    public static boolean SaveFileJson(String file_path, String file_name, JSONObject jsonObject) {
        try {
            File file = new File(file_path);
            if (!file.exists()) {
                file.mkdirs();
            }

            byte[] bytes = jsonObject.toJSONString().getBytes();
            FileOutputStream outputStream = new FileOutputStream(new File(file_path + file_name));
            outputStream.write(bytes);
            outputStream.close();

            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static Map getMapperJsonFromFile(String file_path, String file_name) {
        try {

            File file = new File(file_path + file_name);
            FileInputStream in = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String jsonStr = "";
            String tmp = null;

            while ((tmp = bufferedReader.readLine()) != null) {
                jsonStr += tmp;
            }

            bufferedReader.close();

            if (in != null) {
                in.close();
            }

            String str =jsonStr.substring(1, jsonStr.length() - 1);
            String[] strs = str.split(",");
            Map map = new HashMap();
            for (String string : strs) {
                String key = string.split("=")[0].trim();
                String value = string.split("=")[1];
                map.put(key, value);
            }

            return map;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject GetJSONFromFile(String file_path, String file_name) {
        try {
            File file = new File(file_path + file_name);
            FileInputStream in = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String jsonStr = "";
            String tmp = null;

            while ((tmp = bufferedReader.readLine()) != null) {
                jsonStr += tmp;
            }

            bufferedReader.close();

            if (in != null) {
                in.close();
            }

            return JSONObject.parseObject(jsonStr);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void RemoveFile(String file_path, String file_name) {
        try {
            File file = new File(file_path + file_name);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String GetBase64FromFile(String file_path,String file_name){
        try{
            File file = new File(file_path+file_name);
            FileInputStream in = new FileInputStream(file_path+file_name);
            byte[] bytes = new byte[(int) file.length()];
            in.read(bytes);
            String base64 = new String(Base64.getEncoder().encode(bytes), "UTF-8");


            if (in!=null){
                in.close();
            }

            return base64;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

