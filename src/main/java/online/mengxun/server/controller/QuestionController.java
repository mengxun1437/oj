package online.mengxun.server.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import com.sun.scenario.effect.impl.state.AccessHelper;
import online.mengxun.server.entity.Question;
import online.mengxun.server.entity.Teacher;
import online.mengxun.server.repository.QuestionRepository;
import online.mengxun.server.repository.TeacherRepository;
import online.mengxun.server.response.Check;
import online.mengxun.server.response.Response;
import online.mengxun.server.utils.FileOP;
import org.bouncycastle.est.CACertsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    private String localCodePath="/web/oj/localCode/";


    //增加一个题目
    @PutMapping("/")
    public Response addNewQuesion(@RequestBody JSONObject jsonObject){
//        System.out.println(jsonObject);
        try {
            Check check = new Check();

            if (check.noKey(jsonObject, "name")
                    || check.noKey(jsonObject, "detail")
                    || check.noKey(jsonObject, "diff")
                    || check.noKey(jsonObject, "creator")) {
                return Response.error("提交参数缺失");
            }

            String name = jsonObject.getString("name");
            String detail = jsonObject.getString("detail");
            String creator = jsonObject.getString("creator");
            Integer diff = jsonObject.getInteger("diff");


            if (check.emptyStr(name) || check.emptyStr(detail) || check.emptyStr(creator)) {
                return Response.error("必须提交的参数不能为空");
            }

            if (diff != 0 && diff != 1 && diff != 2) {
                return Response.error("diff提交的参数不符合要求");
            }

            String testdata="";
            if (!check.noKey(jsonObject,"testdata")&&!check.emptyStr("testdata")){
                testdata=jsonObject.getString("testdata");
            }

            String id = UUID.randomUUID().toString();

            //如果提供的code不是空的需要保存文件，并将文件地址保存在数据库
            if (!check.noKey(jsonObject,"code")) {

                if(!check.noKey(jsonObject,"codetype")) {
                    String code = jsonObject.getString("code");
                    String codetype = jsonObject.getString("codetype");

                    if (check.emptyStr(code) || check.emptyStr(codetype)) {
                        return Response.error("提交的code和codetype不能为空");
                    }

                    byte[] data = Base64.getDecoder().decode(code);

                    File file = new File(localCodePath + creator);
                    if (!file.exists()) {
                        file.mkdirs();
                    }


                    OutputStream outputStream = new FileOutputStream(localCodePath + creator + "/" + id + "." + codetype);

                    outputStream.write(data);
                    outputStream.close();
                }else {
                    return Response.error("codetype参数缺失");
                }

            }

            Question question = new Question();

            question.setId(id);
            question.setName(name);
            question.setDetail(detail);
            question.setTestdata(testdata);
            question.setCreator(creator);
            question.setDiff(diff);
            question.setCreate_at(new Date());
            question.setUpdate_at(question.getCreate_at());

            JSONObject jsonQ=new JSONObject();
            jsonQ.put("ID",id);
            jsonQ.put("Name",name);
            jsonQ.put("Detail",detail);
            jsonQ.put("Creator",creator);
            jsonQ.put("TestData",testdata);
            jsonQ.put("Diff",diff);
            jsonQ.put("CreateAt",question.getCreate_at());
            jsonQ.put("UpdateAt",question.getUpdate_at());

            if (questionRepository.save(question) != null) {
                return Response.success("保存成功",jsonQ);
            }else{
                return Response.error("保存失败");
            }


        }catch (Exception e){
//            e.printStackTrace();
            return Response.error();
        }
    }

    //查看某个题目的具体信息,不含代码样例
    @GetMapping("/{id}")
    public Response getQuestionDetail(@PathVariable("id") String id,
                                      @RequestBody JSONObject jsonObject){

        try{
            //首先查看这个题目是否存在
            if(!questionRepository.existsById(id)){
                return Response.error("题目不存在");
            }

            Check check=new Check();
            if (check.noKey(jsonObject,"identity")){
                return Response.error("必须说明用户身份：teacher/student");
            }
            String identity=jsonObject.getString("identity");
            if (check.emptyStr(identity)){
                return Response.error("用户身份不可为空");
            }

            JSONObject jsonQ=new JSONObject();

            Question question=questionRepository.getOne(id);



            jsonQ.put("ID",id);
            jsonQ.put("Name",question.getName());
            jsonQ.put("Detail",question.getDetail());
            jsonQ.put("Creator",question.getCreator());
            jsonQ.put("Diff",question.getDiff());
            jsonQ.put("CreateAt",question.getCreate_at());
            jsonQ.put("UpdateAt",question.getUpdate_at());

            if (identity.equals("teacher")){
                jsonQ.put("TestData",question.getTestdata());
            }

            if (!check.noKey(jsonObject,"codetype")){
                if (check.emptyStr(jsonObject.getString("codetype"))){
                    return Response.error("codetype不可为空");
                }
                File file=new File(localCodePath+question.getCreator()+"/"+id+"."+jsonObject.getString("codetype"));
                if (file.exists()&&file.isFile()){
                    FileInputStream in = new FileInputStream(localCodePath+question.getCreator()+"/"+id+"."+jsonObject.getString("codetype"));
                    byte[] bytes = new byte[(int) file.length()];
                    in.read(bytes);
                    String base64 = new String(Base64.getEncoder().encode(bytes), "UTF-8");


                    if (in!=null){
                        in.close();
                    }

                    jsonQ.put("Code",base64);
                }else {
                    jsonQ.put("Code",null);
                }

                jsonQ.put("CodeType",jsonObject.getString("codetype"));
            }
            return Response.success("题目存在",jsonQ);
        } catch (Exception e){
            return Response.error();
        }
    }

    //修改一个题目的信息
    @PatchMapping("/{id}")
    public Response modifyQuestion(@PathVariable("id") String id,
                                   @RequestBody JSONObject jsonObject){
        try{

            //检测题目id是否存在
            if(!questionRepository.existsById(id)){
                return Response.error("题目不存在");
            }


            Check check=new Check();
            if (check.noKey(jsonObject,"creator")){
                return Response.error("creator参数缺失");
            }

            String creator=jsonObject.getString("creator");
            if (check.emptyStr(creator)){
                return Response.error("提交的creator不能为空");
            }

            Question question=questionRepository.getOne(id);

            if (!question.getCreator().equals(creator)){
                return Response.error("用户校验失败");
            }

            //查看需要patch的参数
            if (!check.noKey(jsonObject,"name")){
                String name=jsonObject.getString("name");
                if (check.emptyStr(name)){
                    return Response.error("提交的name不能为空");
                }
                question.setName(name);
            }
            if (!check.noKey(jsonObject,"detail")){
                String detail=jsonObject.getString("detail");
                if (check.emptyStr(detail)){
                    return Response.error("提交的detail不能为空");
                }
                question.setDetail(detail);
            }
            if (!check.noKey(jsonObject,"testdata")){
                String testdata=jsonObject.getString("testdata");
                if (check.emptyStr(testdata)){
                    return Response.error("提交的testdata不能为空");
                }
                question.setTestdata(testdata);
            }
            if (!check.noKey(jsonObject,"diff")){
                Integer diff=jsonObject.getInteger("diff");
                if (diff!=0&&diff!=1&&diff!=2){
                    return Response.error("提交的diff不符合条件");
                }
                question.setDiff(diff);
            }

            question.setUpdate_at(new Date());

            if (!check.noKey(jsonObject,"code")){
                if (check.noKey(jsonObject,"codetype")){
                    return Response.error("提交code时需指定codetype");
                }
                String code=jsonObject.getString("code");
                String codetype=jsonObject.getString("codetype");
                if(check.emptyStr(code)||check.emptyStr(codetype)){
                    return Response.error("提交的code和codetype不能为空");
                }
                //将base64形式的code写入到文件中
                byte[] data = Base64.getDecoder().decode(code);

                File file = new File(localCodePath + creator);
                if (!file.exists()) {
                    file.mkdirs();
                }


                OutputStream outputStream = new FileOutputStream(localCodePath + creator + "/" + id + "." + codetype);

                outputStream.write(data);
                outputStream.close();

            }

            if (questionRepository.save(question)!=null){
                return Response.success("修改题目信息成功");
            }else{
                return Response.error("修改题目信息失败");
            }

        }catch (Exception e){
            return Response.error();
        }
    }

    //删除一个题目
    @DeleteMapping("/{id}")
    public Response deleteQuestion(@PathVariable("id") String id,
                                   @RequestBody JSONObject jsonObject){
        try{
            //检测题目id是否存在
            if(!questionRepository.existsById(id)){
                return Response.error("题目不存在");
            }

            Check check=new Check();

            //用户校验
            if(check.noKey(jsonObject,"creator")){
                return Response.error("提交的creator参数缺失");
            }
            String creator=jsonObject.getString("creator");
            if (check.emptyStr(creator)){
                return Response.error("提交的creator不能为空");
            }

            Question question=questionRepository.getOne(id);

            if (!question.getCreator().equals(creator)){
                return Response.error("用户校验失败");
            }

            //删除这道题所对应的代码文件
            File file=new File(localCodePath+creator);
            System.out.println(file.exists());
            if (file.exists()&&file.isDirectory()){
                //获取列表
                File[] tmpList=file.listFiles();

                for (File t:tmpList){
                    if (t.isFile()&&t.getName().startsWith(id)){
                        t.delete();
                    }
                }
            }

            questionRepository.deleteById(id);

            return Response.success("删除题目成功");

        }catch (Exception e){
            return Response.error();
        }
    }
}
