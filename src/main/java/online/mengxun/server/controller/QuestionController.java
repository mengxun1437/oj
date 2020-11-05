package online.mengxun.server.controller;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.alibaba.fastjson.JSONObject;
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

                    jsonQ.put("code",base64);
                }else {
                    jsonQ.put("code",null);
                }

                jsonQ.put("codetype",jsonObject.getString("codetype"));
            }
            return Response.success("题目存在",jsonQ);
        } catch (Exception e){
            return Response.error();
        }
    }
}
