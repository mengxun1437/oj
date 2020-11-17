package online.mengxun.server.controller;

import com.mysql.jdbc.util.Base64Decoder;
import sun.misc.BASE64Decoder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ser.Serializers;
import online.mengxun.server.response.Check;
import online.mengxun.server.response.Response;
import online.mengxun.server.utils.FileOP;
import org.apache.catalina.mapper.Mapper;
import org.eclipse.jgit.api.Git;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;
import sun.misc.IOUtils;

import java.io.*;
import java.util.*;

import static online.mengxun.server.controller.GitController.*;


@RestController
@RequestMapping("/code")
public class CodeController {

    public static String repoPath="/web/oj/repo/";

    //提交代码
    @PutMapping("/{id}")
    public Response UserCommit(@PathVariable("id") String id,
                               @RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();
            if (check.noKey(jsonObject,"codetype")
                    ||check.noKey(jsonObject,"code")
                    ||check.noKey(jsonObject,"qid")
                    ||check.noKey(jsonObject,"state")){
                return Response.error("提交数据缺失");
            }

            String code=jsonObject.getString("code");
            String codetype=jsonObject.getString("codetype");
            String qid=jsonObject.getString("qid");
            String state=jsonObject.getString("state");

            if (check.emptyStr(code)||check.emptyStr(codetype)||check.emptyStr(qid)||check.emptyStr(state)){
                return Response.error("提交参数不能为空");
            }

            String targetPath=repoPath+qid;

            File file=new File(targetPath);

            if (!file.exists()){
                file.mkdirs();
            }

            Git git= GitOpenRepo(repoPath,qid);

//            System.out.println(git);


            CheckOut(git,id);

            //将base64解码并保存到文件中
            byte[] data= Base64.getDecoder().decode(code);


            OutputStream outputStream=new FileOutputStream(targetPath+File.separator+"code");
            outputStream.write(data);
            outputStream.close();





            JSONObject jsonR=new JSONObject();
            jsonR.put("CodeType",codetype);
            jsonR.put("AccessState",state);




            byte[] result=jsonR.toJSONString().getBytes();
            outputStream=new FileOutputStream(targetPath+File.separator+"result");
            outputStream.write(result);
            outputStream.close();



            git.add().addFilepattern(".").call();
            git.commit().setMessage(id+"\tcommit\t"+new Date().toString()).call();


            //回到主分支
            BackToMaster(git);

//            System.out.println(GitController.GetAllCommit(git,id));

            return Response.success();


        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }

    //获取用户提交的所有版本的版本号
    @PostMapping("/{id}")
    public Response GetUserAllCommit(@PathVariable("id") String id,
                                     @RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();
            if (check.noKey(jsonObject,"qid")){
                return Response.error("提交数据缺失");
            }

            String qid=jsonObject.getString("qid");

            if (check.emptyStr(qid)){
                return Response.error("qid不能为空");
            }

            Git git=GitController.GitOpenRepo(repoPath,qid);

            ArrayList arrayList=GitController.GetAllCommit(git,id);
//            System.out.println("arraylist\t"+arrayList);



            //最后将分支切回主分支
            BackToMaster(git);

            JSONObject jsonC=new JSONObject();
            jsonC.put("ID",id);
            jsonC.put("QID",qid);
            jsonC.put("Versions",arrayList);



            return Response.success(jsonC);

        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }

    //查看用户某个历史版本具体信息
    @PostMapping("/{id}/{version}")
    public Response GetCommitDetail(@PathVariable("id") String id,
                                    @PathVariable("version") String version,
                                    @RequestBody JSONObject jsonObject){
        try{

            Check check=new Check();
            if (check.noKey(jsonObject,"qid")){
                return Response.error("提交数据缺失");
            }

            String qid=jsonObject.getString("qid");

            Git git=GitController.GitOpenRepo(repoPath,qid);

            VersionBack(git,id,version);

            File file = new File(repoPath+qid+File.separator+"code");
            FileInputStream in = new FileInputStream(repoPath+qid+File.separator+"code");
            byte[] bytes = new byte[(int) file.length()];
            in.read(bytes);
            String base64 = new String(Base64.getEncoder().encode(bytes), "UTF-8");


            if (in!=null){
                in.close();
            }


            file =new File(repoPath+qid+File.separator+"result");
            in=new FileInputStream(repoPath+qid+File.separator+"result");
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



//            System.out.println(bytes);

//            git.add().addFilepattern(".").call();
//            git.commit().setMessage("io流").call();


            //最后将分支切回主分支
            BackToMaster(git);


            JSONObject jsonQ=new JSONObject();

            jsonQ.put("ID",id);
            jsonQ.put("Version",version);
            jsonQ.put("Code",base64);
            jsonQ.put("Result",JSONObject.parseObject(jsonStr));


            return Response.success(jsonQ);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }
}
