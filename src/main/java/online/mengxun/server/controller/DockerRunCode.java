package online.mengxun.server.controller;


import java.io.File;
import java.io.IOException;

public class DockerRunCode {

    public static String localJudgePath="D:\\onlineJudge\\";

    public static File[] getTargetInOutDataNumber(String qid,Boolean isGetInNumber){
        try{
            String target="";
            if (isGetInNumber) {
                target=localJudgePath+qid+"\\in\\";
            } else{
                target=localJudgePath+qid+"\\out\\";
            }

            File file=new File(target);
            if (!file.exists()){
                file.mkdirs();
            }

            File[] files=file.listFiles();

            return files;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static void roundRunCode(String qid,String codePath,String targetOutputPath) {
        try {
            File codeFile = new File(codePath);
            File targetOutputFile = new File(targetOutputPath);
            String inDataPath=localJudgePath+qid+"\\in\\";

            if (!codeFile.exists()) {
                codeFile.mkdirs();
            } else if (!targetOutputFile.exists()) {
                targetOutputFile.mkdirs();
            }

            //打包code.c代码
//            Runtime.getRuntime().exec("d:");
//            Runtime.getRuntime().exec("cd "+codePath);
//            String shellGcc = "gcc code.c";
//            Runtime.getRuntime().exec(shellGcc);

            //获取测试数据的个数
            File[] inDataList=getTargetInOutDataNumber(qid,true);
            File[] outDataList=getTargetInOutDataNumber(qid,false);


            if (inDataList.length==0&&outDataList.length==1){
                File outFile=outDataList[0];
                String shellRunCode="a.exe "+">"+targetOutputPath+outFile.getName();
                Runtime.getRuntime().exec(shellRunCode);
            }else if (inDataList.length!=0&&inDataList.length==outDataList.length){
               for (int i=0;i<inDataList.length;i++){
                   String inFilePath=inDataPath+inDataList[i].getName();
                   String outFilePath=targetOutputPath+inDataList[i].getName();

                   String shellRunCode="a.exe "+"<"+inFilePath+" >"+outFilePath;
                   Runtime.getRuntime().exec(shellRunCode);
               }
            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }
}


