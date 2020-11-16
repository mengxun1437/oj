package online.mengxun.server.controller;

import org.junit.Test;

import javax.print.Doc;

import static org.junit.Assert.*;

public class DockerRunCodeTest {
    @Test
    public void TestRunCode() {
        String codeInPath="D:\\localTest\\bce7768b-398e-4efd-a165-fbc27eeff6d8\\in";
        String codeOutPath="D:\\localTest\\bce7768b-398e-4efd-a165-fbc27eeff6d8\\out";
        String targetOutPath="C:\\Users\\wujun.wang\\Desktop\\Runspace";
//        DockerRunCode.MoveInputfilesToRunspace(codeInPath, targetOutPath);
//        DockerRunCode.RunCode(targetOutPath);
        DockerRunCode.getCodeRunRes(codeOutPath,targetOutPath+"\\out");
    }

}