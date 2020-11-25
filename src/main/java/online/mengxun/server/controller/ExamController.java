package online.mengxun.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.regexp.internal.RE;
import online.mengxun.server.entity.Exam;
import online.mengxun.server.repository.ExamRepository;
import online.mengxun.server.response.Check;
import online.mengxun.server.response.Response;
import online.mengxun.server.utils.FileOP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@RestController
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private ExamRepository examRepository;
    public static String local_exam_base_cid="/web/oj/exam/baseCid/";

    public static String local_exam_base_sid="/web/oj/exam/baseSid/";

    public static String local_exam_base_eid="/web/oj/exam/baseEid/";



    //教师发布考试
    @PutMapping("/")
    public Response addNewExam(@RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();

            if (check.noKey(jsonObject,"creator")){
                return  Response.error("creator参数缺失");
            }else if (check.noKey(jsonObject,"name")){
                return Response.error("name参数缺失");
            }else if (check.noKey(jsonObject,"start")||check.noKey(jsonObject,"end")){
                return Response.error("起始时间未指定");
            }else if (check.noKey(jsonObject,"pub")){
                return Response.error("pub参数缺失");
            }else if (check.noKey(jsonObject,"qids")){
                return Response.error("题目列表缺失");
            }

            String creator=jsonObject.getString("creator");
            String name=jsonObject.getString("name");
            Date start=jsonObject.getDate("start");
            Date end=jsonObject.getDate("end");
            Integer pub=jsonObject.getInteger("pub");
            System.out.println(pub);

            JSONArray qids = jsonObject.getJSONArray("qids");


            if (check.emptyStr(creator)
                    ||check.emptyStr(name)
                    ||check.emptyStr(start.toString())
                    ||check.emptyStr(end.toString())
            ){
                return Response.error("提交参数不能为空");
            }

            if (!start.before(end)){
                return Response.error("时间先后错误");
            }

            if (pub!=0&&pub!=1){
                return Response.error("pub只能是0或1");
            }


            if (qids.size()==0){
                return Response.error("题目列表不能为空");
            }


            Exam exam = new Exam();

            String e_id= UUID.randomUUID().toString();

            exam.setId(e_id);
            exam.setName(name);
            exam.setCreator(creator);
            exam.setStart_time(start);
            exam.setEnd_time(end);
            exam.setPub(pub);

            exam.setCreate_at(new Date());
            exam.setUpdate_at(exam.getCreate_at());

            JSONObject jsonE=new JSONObject();
            jsonE.put("qids",qids);



            boolean suc=FileOP.SaveFileJson(local_exam_base_cid+creator+ File.separator,e_id,jsonE);

            if (suc){
                if (examRepository.save(exam)!=null){
                    JSONObject jsonRes=new JSONObject();
                    jsonRes.put("ID",exam.getId());
                    jsonRes.put("Name",exam.getName());
                    jsonRes.put("Start",exam.getStart_time());
                    jsonRes.put("End",exam.getEnd_time());
                    jsonRes.put("Pub",exam.getPub());
                    jsonRes.put("CreateAt",exam.getCreate_at());
                    jsonRes.put("UpdateAt",exam.getUpdate_at());
                    return Response.success("保存考试成功",jsonRes);
                }else {
                    return Response.error("保存考试失败");
                }
            }else{
                return Response.error("保存试题失败");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }

    //通过id获取考试信息
    @PostMapping("/{id}")
    public Response getExamById(@PathVariable("id") String id,
                                @RequestBody JSONObject jsonObject){
        try {

            //检查id是否存在
            if (!examRepository.existsById(id)){
                return Response.error("没有这场考试");
            }

            Exam exam=examRepository.getOne(id);
            JSONObject jsonRes=new JSONObject();
            jsonRes.put("ID",exam.getId());
            jsonRes.put("Creator",exam.getCreator());
            jsonRes.put("Name",exam.getName());
            jsonRes.put("Start",exam.getStart_time());
            jsonRes.put("End",exam.getEnd_time());
            jsonRes.put("State",exam.getState());
            jsonRes.put("Pub",exam.getPub());
            jsonRes.put("CreateAt",exam.getCreate_at());
            jsonRes.put("UpdateAt",exam.getUpdate_at());
            jsonRes.put("Qids",FileOP.GetJSONFromFile(local_exam_base_cid+exam.getCreator()+File.separator,id).getJSONArray("qids"));


            Check check=new Check();
            if (check.noKey(jsonObject,"identity")){
                return Response.error("identity参数缺失");
            }

            String identity=jsonObject.getString("identity");

            if (check.emptyStr(identity)){
                return Response.error("identity不可为空");
            }else if (identity.equals("teacher")){
                return Response.success(jsonRes);
            }else if (!identity.equals("student")){
                return Response.error("identity限制：teacher/student");
            }

            if (exam.getPub()==1){
                return Response.success(jsonRes);
            }else {
                return Response.error("未找到此题");
            }

        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }


    //修改考试信息
    @PatchMapping("/{id}")
    public Response modifyExam(@PathVariable("id") String id,
                               @RequestBody JSONObject jsonObject) {
        try {
            if (!examRepository.existsById(id)) {
                return Response.error("没有这场考试");
            }

            Exam exam = examRepository.getOne(id);

            Check check = new Check();

            if (check.noKey(jsonObject, "creator")) {
                return Response.error("creator参数缺失");
            }
            String creator = jsonObject.getString("creator");
            if (check.emptyStr(creator)) {
                return Response.error("creator参数不能为空");
            }
            if (!creator.equals(exam.getCreator())) {
                return Response.error("creator不合法");
            }
            if (!check.noKey(jsonObject, "name")) {
                String name = jsonObject.getString("name");
                if (check.emptyStr(name)) {
                    return Response.error("name参数为空");
                }
                exam.setName(name);
            }
            if (!check.noKey(jsonObject, "start")&&!check.noKey(jsonObject,"end")) {
                Date start = jsonObject.getDate("start");
                Date end = jsonObject.getDate("end");
                if (check.emptyStr(start.toString())||check.emptyStr(end.toString())) {
                    return Response.error("start或end参数为空");
                }
                if (!start.before(end)){
                    return Response.error("时间先后错误");
                }
                exam.setStart_time(start);
                exam.setEnd_time(end);
            }

            if (!check.noKey(jsonObject, "pub")) {
                Integer pub = jsonObject.getInteger("pub");
                if (check.emptyStr(pub.toString())) {
                    return Response.error("pub参数为空");
                } else if (pub != 0 && pub != 1) {
                    return Response.error("pubc参数只能是0或1");
                }
                exam.setPub(pub);
            }

            if (!check.noKey(jsonObject, "qids")) {
                JSONArray qids = jsonObject.getJSONArray("qids");

                if (qids.size() == 0) {
                    return Response.error("qids参数为空");
                }
                JSONObject jsonE = new JSONObject();
                jsonE.put("qids", qids);
                boolean suc = FileOP.SaveFileJson(local_exam_base_cid + creator + File.separator, id, jsonE);

                if (!suc) {
                    return Response.error("保存题目失败");
                }

            }

            exam.setUpdate_at(new Date());

            if (examRepository.save(exam) != null) {
                return Response.success("修改考试信息成功");
            }else {
                return Response.error("修改考试信息失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return Response.error();
        }
    }


    //删除考试信息
    @DeleteMapping("/{id}")
    public Response delExam(@PathVariable("id") String id,
                            @RequestBody JSONObject jsonObject){
        try {
            if (!examRepository.existsById(id)){
                return Response.error("没有这场考试");
            }

            Exam exam=examRepository.getOne(id);

            Check check=new Check();

            if (check.noKey(jsonObject,"creator")){
                return Response.error("creator参数缺失");
            }
            String creator=jsonObject.getString("creator");
            if (check.emptyStr(creator)){
                return Response.error("creator不能为空");
            }
            if (!creator.equals(exam.getCreator())){
                return Response.error("用户校验不合法");
            }

            examRepository.deleteById(id);

            FileOP.RemoveFile(local_exam_base_cid+creator+File.separator,id);


            return Response.success("删除成功");


        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }


    //学生答题
    @PostMapping("/stu/{id}")
    public Response commitAnswer(@PathVariable("id") String id,
                                 @RequestBody JSONObject jsonObject){
        try {
            if (!examRepository.existsById(id)){
                return Response.error("没有这场考试");
            }
            Exam exam=examRepository.getOne(id);


            if (exam.getPub()==0){
                return Response.error("非法答题");
            }

            Check check=new Check();

            if (check.noKey(jsonObject,"sid")){
                return Response.error("需要提交学生id");
            }
            String sid=jsonObject.getString("sid");
            if (check.emptyStr(sid)){
                return Response.error("学生id不能为空");
            }

            if(check.noKey(jsonObject,"commit")){
                return Response.error("commit缺失");
            }
            JSONArray commit =jsonObject.getJSONArray("commit");

            if (commit.size()==0){
                return Response.error("commit不可为空");
            }


            JSONArray qids=FileOP.GetJSONFromFile(local_exam_base_cid+exam.getCreator()+File.separator,id).getJSONArray("qids");

            if (qids==null){
                return Response.error("教师非法设置考试题目");
            }

            if (commit.size()!=qids.size()){
                return Response.error("考试提交题目数量不一致");
            }

            JSONObject jsonS=new JSONObject();
            jsonS.put("commit",commit);

            boolean suc_s = FileOP.SaveFileJson(local_exam_base_sid + sid + File.separator, id, jsonS);
            boolean suc_e = FileOP.SaveFileJson(local_exam_base_eid + id + File.separator, sid, jsonS);

            if (!suc_s||!suc_e){
                return Response.error("提交失败");
            }

            return Response.success("提交成功");
        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }

    //获取考试记录
    @PostMapping("/list")
    public Response getExamCommitOrPublishList(@RequestBody JSONObject jsonObject){
        try {
            Check check = new Check();
            File[] files;

            if (check.noKey(jsonObject, "identity")) {
                return Response.error("没有指定identity");
            }
            String identity = jsonObject.getString("identity");
            if (check.emptyStr(identity)) {
                return Response.error("identity不能为空");
            }
            if (!identity.equals("student") && !identity.equals("teacher")) {
                return Response.error("identity限制：student/teacher");
            }

            if (identity.equals("student")) {
                if (check.noKey(jsonObject, "sid")) {
                    return Response.error("sid参数缺失");
                }
                String sid = jsonObject.getString("sid");
                if (check.emptyStr(sid)) {
                    return Response.error("sid不能为空");
                }

                files = new File(local_exam_base_sid + sid).listFiles();

                ArrayList<String> arr = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile()) {
                        arr.add(file.getName().split("\\.")[0]);
                    }
                }

                JSONObject jsonR = new JSONObject();
                jsonR.put("Sid", sid);
                jsonR.put("Commits", arr);

                return Response.success(jsonR);
            } else if (identity.equals("teacher")) {
                if (check.noKey(jsonObject, "cid")) {
                    return Response.error("cid参数缺失");
                }
                String cid=jsonObject.getString("cid");
                if (check.emptyStr(cid)){
                    return Response.error("cid不能为空");
                }

                files = new File(local_exam_base_cid + cid).listFiles();

                ArrayList<String> arr = new ArrayList<>();
                for (File file : files) {
                    if (file.isFile()) {
                        arr.add(file.getName().split("\\.")[0]);
                    }
                }

                JSONObject jsonR = new JSONObject();
                jsonR.put("Cid", cid);
                jsonR.put("Publish", arr);

                return Response.success(jsonR);
            }else {
                return Response.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }

    //教师获取某一道题目的答题人
    @PostMapping("/commit/{eid}")
    public Response TeacherGetCommit(@PathVariable("eid") String eid,
                                     @RequestBody JSONObject jsonObject){
        try {
            if (!examRepository.existsById(eid)){
                return Response.error("没有这道题目");
            }
            Exam exam=examRepository.getOne(eid);

            Check check=new Check();

            if (check.noKey(jsonObject,"cid")){
                return Response.error("cid教师id缺失");
            }
            String cid=jsonObject.getString("cid");
            if (check.emptyStr(cid)){
                return Response.error("cid不能为空");
            }

            if (!cid.equals(exam.getCreator())){
                return Response.error("教师身份不合法");
            }

            File[] files = new File(local_exam_base_eid + eid).listFiles();

            ArrayList<String> arr = new ArrayList<>();
            for (File file : files) {
                if (file.isFile()) {
                    arr.add(file.getName().split("\\.")[0]);
                }
            }

            JSONObject jsonR = new JSONObject();
            jsonR.put("Eid", eid);
            jsonR.put("Answerd", arr);

            return Response.success(jsonR);


        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }


    //获取所有的考试
    @GetMapping("/")
    public Response getAllExams(){
        try {
            JSONObject jsonE=new JSONObject();

            ArrayList<JSONObject> arrayList=new ArrayList<>();

            List<Exam> examList=examRepository.findAllByPub(1);

            for (int i=0;i<examList.size();i++){
                jsonE=new JSONObject();
                jsonE.put("Eid",examList.get(i).getId());
                jsonE.put("Name",examList.get(i).getName());
                jsonE.put("Creator",examList.get(i).getCreator());
                jsonE.put("CreateAt",examList.get(i).getCreate_at());
                jsonE.put("UpdateAt",examList.get(i).getUpdate_at());

                arrayList.add(jsonE);
            }

            return Response.success(arrayList);

        }catch (Exception e){
            e.printStackTrace();
            return Response.error();
        }
    }


}
