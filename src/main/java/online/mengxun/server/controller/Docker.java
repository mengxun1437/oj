package online.mengxun.server.controller;

public class Docker {

    //启动一个名字为user的容器
    public static void CreateContainer(String container_name){
        String command="docker run -itd --name "+container_name+" oj";
        DockerRunCode.CommandShell(command);
        System.out.println(command);
    }

    //将in文件传递到user容器中
    public static void CopyFileIntoContainer(String inFilePath,String codeFilePath,String container_name){
        String command_del_in="docker exec -d "+container_name+" rm -rf /usr/local/oj/in";
        String command_copy_in="docker cp "+inFilePath+" "+container_name+":/usr/local/oj";
        String command_copy_code="docker cp "+codeFilePath+" "+container_name+":/usr/local/oj";
        DockerRunCode.CommandShell(command_del_in);
        DockerRunCode.CommandShell(command_copy_in);
        DockerRunCode.CommandShell(command_copy_code);
        System.out.println(command_del_in);
        System.out.println(command_copy_in);
        System.out.println(command_copy_code);
    }

    //代码运行
    public static void RunCodeInContainer(String container_name){
        String command="docker exec -d "+container_name+" java -classpath /usr/local/oj/ Docker";
        DockerRunCode.CommandShell(command);
        System.out.println(command);
    }


    //将out文件从docker运行完以后拷贝出来，但是这个地方存在异步问题，正在解决
    public static void CopyFileFromContainer(String container_name,String outFilePath){
        String command="docker cp "+container_name+":/usr/local/oj/out "+ outFilePath;
        DockerRunCode.CommandShell(command);
        System.out.println(command);
    }
}
