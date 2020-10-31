package online.mengxun.server.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.org.apache.regexp.internal.RE;
import netscape.javascript.JSObject;
import online.mengxun.server.entity.Student;
import online.mengxun.server.entity.Teacher;
import online.mengxun.server.repository.TeacherRepository;
import online.mengxun.server.response.Check;
import online.mengxun.server.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import java.math.BigInteger;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;


    //教师注册
    @PutMapping("/")
    public Response registTeacher(@RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();
            //判断参数是否缺失
            if(check.noKey(jsonObject,"name")||check.noKey(jsonObject,"password")){
                return Response.error(40000,"提交参数缺失");
            }

            String name=jsonObject.getString("name");
            String password=jsonObject.getString("password");

            //非空校验
            if (check.emptyStr(name)||check.emptyStr(password)){
                return Response.error("姓名或密码为空");
            }

            if (name.length()<2||name.length()>6){
                return Response.error("用户名长度不合法");
            }

            if (password.length()<6||password.length()>20){
                return Response.error("密码长度不合法");
            }

             int flag=teacherRepository.findByName(name).size();
             if (flag!=0){
                 return Response.error("用户名已经注册");
             }

             //创建teacher对象
             Teacher teacher=new Teacher();
             String id=UUID.randomUUID().toString();
             teacher.setId(id);
             teacher.setName(name);
             teacher.setPassword(password);

             teacher.setCreate_at(new Date());
             teacher.setUpdate_at(teacher.getCreate_at());

            JSONObject jsonT=new JSONObject();
            jsonT.put("ID",id);
            jsonT.put("Name",name);
            jsonT.put("CreateAt",teacher.getCreate_at());
            jsonT.put("UpdateAt",teacher.getUpdate_at());

             if(teacherRepository.save(teacher)!=null){
                 return Response.success("教师注册成功",jsonT);
             }else{
                 return Response.error("教师注册失败");
             }

        }catch (Exception e){
            return Response.error();
        }
    }


    //教师登录
    @PostMapping("/")
    public Response loginStudent(@RequestBody JSONObject jsonObject){
        try{
            Check check=new Check();
            //判断参数是否缺失
            if(check.noKey(jsonObject,"name")||check.noKey(jsonObject,"password")){
                return Response.error("提交参数缺失");
            }

            String name=jsonObject.getString("name");
            String password=jsonObject.getString("password");

            //非空校验
            if (check.emptyStr(name)||check.emptyStr(password)){
                return Response.error("姓名或密码为空");
            }

            if (name.length()<2||name.length()>6){
                return Response.error("用户名长度不合法");
            }

            if (password.length()<6||password.length()>20){
                return Response.error("密码长度不合法");
            }

            List<Teacher> aimTeacherList = teacherRepository.findByName(name);
            if (aimTeacherList.size()!=1){
                return Response.error("教师不存在");
            }

            Teacher aimTeacher=aimTeacherList.get(0);


            if (!aimTeacher.getPassword().equals(password)){
                return Response.error("教师密码错误");
            }

            JSONObject jsonT=new JSONObject();
            jsonT.put("ID",aimTeacher.getId());
            jsonT.put("Name",aimTeacher.getName());
            jsonT.put("CreateAt",aimTeacher.getCreate_at());
            jsonT.put("UpdateAt",aimTeacher.getUpdate_at());

            return Response.success("教师存在，登录成功",jsonT);

        }catch (Exception e){
            return Response.error();
        }
    }



    //通过id获取教师的所有信息
    @GetMapping("/{id}")
    public Response getTeacherById(@PathVariable("id") String id){
        try{
            //id检查
            if (!teacherRepository.existsById(id)){
                return Response.error("提供的教师id不存在");
            }

            Teacher teacher=teacherRepository.findById(id).get();


            JSONObject jsonObject=new JSONObject();
            jsonObject.put("ID",teacher.getId());
            jsonObject.put("Name",teacher.getName());
            jsonObject.put("Sex",teacher.getSex());
            jsonObject.put("Age",teacher.getAge());
            jsonObject.put("Email",teacher.getEmail());
            jsonObject.put("CreateAt",teacher.getCreate_at());
            jsonObject.put("UpdateAt",teacher.getUpdate_at());


            return Response.success(jsonObject);
        }catch (Exception e){
            return Response.error();
        }
    }

}
