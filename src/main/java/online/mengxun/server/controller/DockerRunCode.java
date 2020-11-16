package online.mengxun.server.controller;


import org.omg.SendingContext.RunTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

public class DockerRunCode {

    public static void MoveInputfilesToRunspace(String path,String targetpath){
        try{
            System.out.println("cmd "+path+" "+targetpath);
            System.out.println(File.separator);

//        判断in文件夹是否存在，不存在就创建,存在就清空
            if (!new File(targetpath+File.separator+"in").exists()){
                new File(targetpath+File.separator+"in").mkdirs();
            }else{
                deleteDirectory(new File(targetpath+File.separator+"in"));
                new File(targetpath+File.separator+"in").mkdirs();
            }
            String cmd="cmd /c copy "+path+" "+targetpath+File.separator+"in";
            Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void RunCode(String targetpath){
        try{

            //编译code.c
            try{
                Runtime.getRuntime().exec("cmd /c gcc "+targetpath+File.separator+"code.c -o "+targetpath+File.separator+"code.exe");
            }catch (Exception e){
                System.out.println("编译失败");
                return;
            }

            //如果没有输出文件夹的话要创建
            if(!new File(targetpath+File.separator+"out").exists()){
                new File(targetpath+File.separator+"out").mkdirs();
            }

            File file=new File(targetpath+File.separator+"in");
            if (new File(targetpath+File.separator+"out").exists()){
                deleteDirectory(new File(targetpath+File.separator+"out"));
                new File(targetpath+File.separator+"out").mkdirs();
            }
            for(File f:file.listFiles()){
                String filename=f.getName();
                System.out.println(filename);
                String fileOut=filename.split("\\.")[0]+".out";
                System.out.println(fileOut);
                Runtime.getRuntime().exec("cmd /c "+targetpath+File.separator+"code.exe <"+targetpath+File.separator+"in"+File.separator+filename+" >"+targetpath+File.separator+"out"+File.separator+fileOut);
                System.out.println("cmd /c "+targetpath+File.separator+"code.exe <"+targetpath+File.separator+"in"+File.separator+filename+" >"+targetpath+File.separator+"out"+File.separator+fileOut);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void getCodeRunRes(String outpath,String targetpath){
        try{

            File outFile=new File(outpath);
            File targetFile=new File(targetpath);

            for (File out:outFile.listFiles()){
                FileInputStream out_stream=new FileInputStream(out);
                byte []out_bytes=new byte[(int)out.length()];
//                System.out.println(out_bytes);
                out_stream.read(out_bytes);
                String out_str =new String(out_bytes, StandardCharsets.UTF_8);

                if (out_stream!=null){
                    out_stream.close();
                }
                for (File target:targetFile.listFiles()){
//                    System.out.println(target.getName());
                    if (out.getName().equals(target.getName())){
                        //读取出文件内容
                        FileInputStream target_stream=new FileInputStream(target);
                        byte []target_bytes=new byte[(int)target.length()];
//                        System.out.println(target_bytes);
                        target_stream.read(target_bytes);
                        if (target_stream!=null){
                            target_stream.close();
                        }
                        String target_str=new String(target_bytes, StandardCharsets.UTF_8);

                        if (out_str.equals(target_str)){
                            System.out.println(out.getName()+"\t结果正确");
                            break;
                        }else{
                            System.out.println(out.getName()+ String.format("\t结果错误\t预期结果%s\t运行结果%s",out_str,target_str ));
                        }
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}


