package online.mengxun.server.controller;


import online.mengxun.server.response.Response;
import org.omg.SendingContext.RunTime;
import org.springframework.cglib.beans.FixedKeySet;

import java.awt.peer.CanvasPeer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.AccessControlContext;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

public class DockerRunCode {

    public static String localTestDataPath="/web/oj/localTestData/";
    public static String tmplocalRunPath="/web/oj/localRun/";
    public static Integer CompileErrorCode=1;
    public static  Integer RunErrorCode=2;
    public static  Integer RunSuccessCode=3;
    public static  Integer AcceptedCode=4;
    public static  Integer WrongCode=5;



    public static void CommandShell(String command){
        try{
            Process process=Runtime.getRuntime().exec(new String[]{"/bin/sh","-c",command});

            process.waitFor();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void MoveInputfilesToRunspace(String path,String targetpath){
        try{

//        判断in文件夹是否存在，不存在就创建,存在就清空
            if (!new File(targetpath+File.separator+"in").exists()){
                new File(targetpath+File.separator+"in").mkdirs();
            }

            String command_cp="cp -r "+path+" "+targetpath+File.separator;
            CommandShell(command_cp);
            System.out.println(command_cp);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static Integer RunCode(String targetpath,String container_name){
        try{
          //如果没有输出文件夹的话要创建
            if(!new File(targetpath+File.separator+"out").exists()){
                new File(targetpath+File.separator+"out").mkdirs();
            }

       //编译code.c
            String command_compiler="gcc "+targetpath+File.separator+"code.c -o "+targetpath+File.separator+"code";
            CommandShell(command_compiler);
            System.out.println(command_compiler);

            boolean has_compiled=false;

            for (File tmpFile:new File(targetpath).listFiles()){
                if (tmpFile.getName().equals("code")){
                    has_compiled=true;
                    break;
                }
            }

            if (!has_compiled){
                return CompileErrorCode;
            }

//            File file=new File(targetpath+File.separator+"in");
//
//
//            for(File f:file.listFiles()){
//                String filename=f.getName();
//                System.out.println(filename);
//                String fileOut=filename.split("\\.")[0]+".out";
//                System.out.println(fileOut);
//                String command_inout=targetpath+File.separator+"code <"+targetpath+File.separator+"in"+File.separator+filename+" >"+targetpath+File.separator+"out"+File.separator+fileOut;
//                CommandShell(command_inout);
//                System.out.println(command_inout);
//            }
            //在容器中运行代码
            Docker.CreateContainer(container_name);
            Docker.CopyFileIntoContainer(targetpath+"in",targetpath+"code",container_name);
            Docker.RunCodeInContainer(container_name);
            Docker.CopyFileFromContainer(container_name,targetpath);

            return RunSuccessCode;
        }catch (Exception e){
            e.printStackTrace();
            return RunErrorCode;
        }
    }


    public static Integer getCodeRunRes(String outpath,String targetpath){

        try{

            File outFile=new File(outpath);
            File targetFile=new File(targetpath);

            for (File out:outFile.listFiles()){
                boolean out_flag=false;
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
                        out_flag=true;
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
                            return WrongCode;
                        }
                    }
                }
                if (!out_flag){
                    return WrongCode;
                }
            }

            return AcceptedCode;

        }catch (Exception e){
            e.printStackTrace();
            return RunErrorCode;
        }
    }

}


