package online.mengxun.server.controller;

import com.alibaba.fastjson.JSONObject;
import online.mengxun.server.response.Check;
import online.mengxun.server.response.Response;
import online.mengxun.server.utils.FileOP;
import org.apache.catalina.mapper.Mapper;
import org.eclipse.jgit.api.Git;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

import static online.mengxun.server.controller.GitController.*;


@RestController
@RequestMapping("/code")
public class CodeController {

    public static String repoPath="D:\\repo1\\";

    //提交代码
    @PutMapping("/{id}")
    public Response UserCommit(@PathVariable("id") String id,
                               @RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();
            if (check.noKey(jsonObject,"type")
                    ||check.noKey(jsonObject,"file")
                    ||check.noKey(jsonObject,"repoName")){
                return Response.error("提交数据缺失");
            }

            String file=jsonObject.getString("file");
            String type=jsonObject.getString("type");
            String repoName=jsonObject.getString("repoName");

            String targetPath=repoPath+repoName;

            Git git=GitController.AddNewGitRepo(repoPath,repoName);
            System.out.println(git);


            CheckOut(git,id);

            //将base64解码并保存到文件中
            byte[] data= Base64.getDecoder().decode(file);

            FileOP.delAllFile(targetPath);

            OutputStream outputStream=new FileOutputStream(targetPath+"\\code.txt");
            outputStream.write(data);
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
    @GetMapping("/{id}")
    public Response GetUserAllCommit(@PathVariable("id") String id,
                                     @RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();
            if (check.noKey(jsonObject,"repoName")){
                return Response.error("提交数据缺失");
            }

            String repoName=jsonObject.getString("repoName");

            Git git=GitController.AddNewGitRepo(repoPath,repoName);

            ArrayList arrayList=GitController.GetAllCommit(git,id);
            System.out.println("arraylist\t"+arrayList);



            //最后将分支切回主分支
            BackToMaster(git);

            Map<String,Object> map= new HashMap<String,Object>();
            map.put("id",id);
            map.put("version",arrayList);



            return Response.success(map);

        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }

    //查看用户某个历史版本具体信息
    @GetMapping("/{id}/{version}")
    public Response GetCommitDetail(@PathVariable("id") String id,
                                    @PathVariable("version") String version,
                                    @RequestBody JSONObject jsonObject){
        try{

            Check check=new Check();
            if (check.noKey(jsonObject,"type")
                ||check.noKey(jsonObject,"repoName")){
                return Response.error("提交数据缺失");
            }

            String type=jsonObject.getString("type");
            String repoName=jsonObject.getString("repoName");

            Git git=GitController.AddNewGitRepo(repoPath,repoName);

            VersionBack(git,id,version);

            File file = new File(repoPath+repoName+"\\code.txt");
            FileInputStream in = new FileInputStream(repoPath+repoName+"\\code.txt");
            byte[] bytes = new byte[(int) file.length()];
            in.read(bytes);
            String base64 = new String(Base64.getEncoder().encode(bytes), "UTF-8");


            if (in!=null){
                in.close();
            }

            System.out.println(bytes);

//            git.add().addFilepattern(".").call();
//            git.commit().setMessage("io流").call();


            //最后将分支切回主分支
            BackToMaster(git);


            Map<String,Object> map=new HashMap<>();

            map.put("id",id);
            map.put("version",version);
            map.put("type",type);
            map.put("file",base64);


            return Response.success(map);
        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }
}
