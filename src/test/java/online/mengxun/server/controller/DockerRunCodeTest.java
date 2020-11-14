package online.mengxun.server.controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class DockerRunCodeTest {
    @Test
    public void TestRunCode(){
        String qid="question1";
        String codePath="D:\\RunCode\\user1\\";
        String targetOutPath="D:\\RunedCodeResult\\";

        DockerRunCode.roundRunCode(qid,codePath,targetOutPath);
    }


}