package online.mengxun.server.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response  implements Serializable {
     private Integer code;
     private String msg;
     private Object data;

     public static Response success(int code, String msg, Object data){
         Response response=new Response();
         response.code=code;
         response.msg=msg;
         response.data=data;
         return  response;
     }

     public static Response success(){
        Response response=new Response();
        response.code=0;
        response.msg="success";
        response.data=null;
        return  response;
    }

    public static Response success(String msg, Object data){
        Response response=new Response();
        response.code=0;
        response.msg=msg;
        response.data=data;
        return  response;
    }

    public static Response success(String msg){
        Response response=new Response();
        response.code=0;
        response.msg=msg;
        response.data=null;
        return  response;
    }

    public static Response success(Object data){
        Response response=new Response();
        response.code=0;
        response.msg="success";
        response.data=data;
        return  response;
    }

    public static Response error(int code, String msg, Object data){
        Response response=new Response();
        response.code=code;
        response.msg=msg;
        response.data=data;
        return  response;
    }

    public static Response error(int code, String msg){
        Response response=new Response();
        response.code=code;
        response.msg=msg;
        response.data=null;
        return  response;
    }

    public static Response error(String msg){
        Response response=new Response();
        response.code=40000;
        response.msg=msg;
        response.data=null;
        return  response;
    }

    public static Response error(){
        Response response=new Response();
        response.code=40000;
        response.msg="error";
        response.data=null;
        return  response;
    }
}
