package online.mengxun.server.controller;


import com.alibaba.fastjson.JSONObject;
import online.mengxun.server.entity.Student;
import online.mengxun.server.entity.Teacher;
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

            if (name.length()<2||name.length()>6){
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

            if (name.length()<2||name.length()>6){
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


    //通过id获取学生的所有信息
    @GetMapping("/{id}")
    public Response getTeacherById(@PathVariable("id") String id) {
        try {
            //id检查
            if (!studentRepository.existsById(id)) {
                return Response.error("提供的学生id不存在");
            }

            Student student = studentRepository.findById(id).get();


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", student.getId());
            jsonObject.put("Name", student.getName());
            jsonObject.put("Email", student.getEmail());
            jsonObject.put("CreateAt", student.getCreate_at());
            jsonObject.put("UpdateAt", student.getUpdate_at());


            return Response.success(jsonObject);
        } catch (Exception e) {
            return Response.error();
        }
    }


    //修改教师的个人信息
    @PatchMapping("/{id}")
    public Response Modify(@PathVariable("id") String id,
                           @RequestBody JSONObject jsonObject) {
        try {
            //id检查
            if (!studentRepository.existsById(id)) {
                return Response.error("提供的学生id不存在");
            }

            Student student = studentRepository.findById(id).get();

            //获取用户想要修改的参数
            Check check = new Check();
            if (!check.noKey(jsonObject, "name")) {
                if (check.emptyStr(jsonObject.getString("name"))) {
                    return Response.error("提供的用户名参数为空");
                }else if (jsonObject.getString("name").length()<2
                        ||jsonObject.getString("name").length()>6){
                    return Response.error("用户名长度错误");
                }else if (studentRepository.findByName(jsonObject.getString("name")).size()!=0){
                    return Response.error("用户名已被注册");
                }
                student.setName(jsonObject.getString("name"));
            }
            if (!check.noKey(jsonObject, "password")) {
                if (check.emptyStr(jsonObject.getString("password"))) {
                    return Response.error("提供的密码参数为空");
                }else if (jsonObject.getString("password").length()<6
                        ||jsonObject.getString("password").length()>20){
                    return Response.error("用户密码长度错误");
                }
                student.setPassword(jsonObject.getString("password"));
            }
            if (!check.noKey(jsonObject, "email")) {
                if (check.emptyStr(jsonObject.getString("email"))) {
                    return Response.error("提供的邮箱参数为空");
                }
                student.setEmail(jsonObject.getString("email"));
            }


            student.setUpdate_at(new Date());

            if (studentRepository.save(student)!=null){
                JSONObject _jsonObject = new JSONObject();
                _jsonObject.put("ID", student.getId());
                _jsonObject.put("Name", student.getName());
                _jsonObject.put("Email", student.getEmail());
                _jsonObject.put("CreateAt", student.getCreate_at());
                _jsonObject.put("UpdateAt", student.getUpdate_at());
                return Response.success("学生信息修改成功",_jsonObject);
            }else{
                return Response.error("学生信息修改失败");
            }
        } catch (Exception e) {
            return Response.error();
        }
    }

}
