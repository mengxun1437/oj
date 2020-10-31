package online.mengxun.server.controller;


import com.alibaba.fastjson.JSONObject;
import online.mengxun.server.entity.Student;
import online.mengxun.server.repository.StudentRepository;
import online.mengxun.server.response.Check;
import online.mengxun.server.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;


    @PutMapping("/")
    public Response registStudent(@RequestBody JSONObject jsonObject){
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

            if (name.length()<6||name.length()>20){
                return Response.error("用户名长度不合法");
            }

            if (password.length()<6||password.length()>20){
                return Response.error("密码长度不合法");
            }

            int flag=studentRepository.findByName(name).size();
            if (flag!=0){
                return Response.error("用户名已经注册");
            }

            //创建student对象
            Student student=new Student();
            String id= UUID.randomUUID().toString();
            student.setId(id);
            student.setName(name);
            student.setPassword(password);
            Date date=new Date();
            String dateString=date.toString();
            student.setCreate_at(date);
            student.setUpdate_at(date);

            JSONObject jsonT=new JSONObject();
            jsonT.put("ID",id);
            jsonT.put("Name",name);
            jsonT.put("CreateAt",dateString);
            jsonT.put("UpdateAt",dateString);

            if(studentRepository.save(student)!=null){
                return Response.success("学生注册成功",jsonT);
            }else{
                return Response.error("学生注册失败");
            }

        }catch (Exception e){
            return Response.error();
        }
    }

    //学生登录
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

            if (name.length()<6||name.length()>20){
                return Response.error("用户名长度不合法");
            }

            if (password.length()<6||password.length()>20){
                return Response.error("密码长度不合法");
            }

            List<Student> aimStudentList = studentRepository.findByName(name);
            if (aimStudentList.size()!=1){
                return Response.error("学生不存在");
            }

            Student aimStudent = aimStudentList.get(0);

            if (!aimStudent.getPassword().equals(password)){
                return Response.error("学生密码错误");
            }

            JSONObject jsonS=new JSONObject();
            jsonS.put("ID",aimStudent.getId());
            jsonS.put("Name",aimStudent.getName());
            jsonS.put("CreateAt",aimStudent.getCreate_at());
            jsonS.put("UpdateAt",aimStudent.getUpdate_at());

            return Response.success("学生存在,登录成功",jsonS);

        }catch (Exception e){
            return Response.error();
        }
    }

}
