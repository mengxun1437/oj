package online.mengxun.server.utils;

import java.io.File;

public class FileOP {

    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        for (String s:tempList) {
            File temp=new File(path+s);
            if (temp!=null&&temp.isFile()){
                temp.delete();
            }
        }
    }
}

