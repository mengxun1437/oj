package online.mengxun.server.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.*;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

public class FileOP {

    public static boolean SaveFileJson(String file_path,String file_name, JSONObject jsonObject){
        try{
            File file=new File(file_path);
            if (!file.exists()){
                file.mkdirs();
            }

            byte[] bytes=jsonObject.toJSONString().getBytes();
            FileOutputStream outputStream = new FileOutputStream(new File(file_path+file_name));
            outputStream.write(bytes);
            outputStream.close();

            return true;


        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject GetJSONFromFile(String file_path,String file_name){
        try{
            File file = new File(file_path+file_name);
            FileInputStream in = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String jsonStr = "";
            String tmp=null;

            while ((tmp = bufferedReader.readLine()) != null) {
                jsonStr+=tmp;
            }

            bufferedReader.close();

            if (in!=null){
                in.close();
            }

            return JSONObject.parseObject(jsonStr);

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void RemoveFile(String file_path,String file_name){
        try{
            File file=new File(file_path+file_name);
            if (file.exists()&&file.isFile()){
                file.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

