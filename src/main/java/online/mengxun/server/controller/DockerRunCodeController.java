package online.mengxun.server.controller;

import com.alibaba.fastjson.JSONObject;
import online.mengxun.server.response.Check;
import online.mengxun.server.response.Response;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import static org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory;

@RestController
@RequestMapping("/run")
public class DockerRunCodeController {


    //提交运行
    @PostMapping("/{id}")
    public Response getRunResult(@PathVariable("id") String qid,
                                 @RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();


            if (check.noKey(jsonObject,"uid")){
                return Response.error("没有提交操作人的id");
            }
            String uid=jsonObject.getString("uid");

            if (check.emptyStr(uid)){
                return Response.error("操作人的id不可为空");
            }

            if (!new File(DockerRunCode.tmplocalRunPath+uid).exists()){
                new File(DockerRunCode.tmplocalRunPath+uid).mkdirs();
            }else{
                deleteDirectory(new File(DockerRunCode.tmplocalRunPath+uid));
                new File(DockerRunCode.tmplocalRunPath+uid).mkdirs();
            }

            //如果提供的code不是空的需要保存文件，并将文件地址保存在数据库
            if (!check.noKey(jsonObject,"code")) {

                if(!check.noKey(jsonObject,"codetype")) {
                    String code = jsonObject.getString("code");
                    String codetype = jsonObject.getString("codetype");
                    if (check.emptyStr(code) || check.emptyStr(codetype)) {
                        return Response.error("提交的code和codetype不能为空");
                    }

                    byte[] data = Base64.getDecoder().decode(code);


                    OutputStream outputStream = new FileOutputStream(DockerRunCode.tmplocalRunPath + uid + File.separator + "code."+codetype);

                    outputStream.write(data);
                    outputStream.close();


                }else {
                    return Response.error("codetype参数缺失");
                }

            }


            if (!new File(DockerRunCode.localTestDataPath+qid).exists()){
                return Response.error("没有此题或者此题没有测试数据");
            }

            DockerRunCode.MoveInputfilesToRunspace(DockerRunCode.localTestDataPath+qid+File.separator+"in", DockerRunCode.tmplocalRunPath+uid);

            Integer status=-1;
            //运行三次解决延迟，后续改进
            for (int i=0;i<3;i++){
                status= DockerRunCode.RunCode(DockerRunCode.tmplocalRunPath+uid+File.separator,uid);
                System.out.println(status);
            }

            switch (status){
                case 1:
                    return Response.success("代码编译错误","CompileError");
                case 2:
                    return Response.success("代码运行时出错","RunError");
                case 3:
                    Integer run_code= DockerRunCode.getCodeRunRes(DockerRunCode.localTestDataPath+qid+File.separator+"out", DockerRunCode.tmplocalRunPath+uid+File.separator+"out");
                    System.out.println(run_code);
                    switch (run_code){
                        case 2:
                            return Response.success("代码运行时出错","RunError");
                        case 5:
                            return Response.success("运行答案错误","WrongAnswer");
                        case 4:
                            return Response.success("运行通过","Accepted");
                        default:
                            return Response.error();
                    }
                default:return Response.error();
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }
}
