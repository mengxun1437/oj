package online.mengxun.server.controller;

import online.mengxun.server.utils.FileOP;
import org.aspectj.apache.bcel.classfile.Code;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GitController {

    //回到主分支
    public static void BackToMaster(Git git){
        try{
            git.checkout().setCreateBranch(false).setName("master").call();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //创建每一道题目的仓库
    public static Git AddNewGitRepo(String repoPath, String repoName){
        Git git=null;
        try{
            //首先判断给的地址是否可以作为仓库，如果不能，就打开这个仓库
            File repo=new File(repoPath);
            if (!repo.exists()&&git==null){
                repo.mkdirs();
                git=Git.init().setDirectory(new File(repoPath+repoName)).call();
                git.add().addFilepattern(".").call();
                git.commit().setMessage("git init").call();
            }else if(repo.exists()){
                git = Git.open(new File(repoPath+repoName));
                BackToMaster(git);
            }
            System.out.println(git);
            return git;
        }catch (Exception e){
            e.printStackTrace();
        }
        return git;
    }


    //用户提交
    public static void CheckOut(Git git,String branch){

        BackToMaster(git);

        //每一个用户在一道题中都是一个分支
        try{
            //判断用户在这道题中有没有存在的分支，没有就创建，有就直接checkout
            List<Ref> refs=git.branchList().call();
            boolean branchExist=false;
            for (Ref ref:refs){
//                System.out.println(ref);
                System.out.println(ref.getName());
                //用户分支存在
                if (ref.getName().split("/")[2].equals(branch)){
//                    System.out.println("分支已存在");
                    branchExist=true;
                    git.checkout().setCreateBranch(false).setName(branch).call();
                    break;
                }
            }
            if (!branchExist){
//                System.out.println("分支新创建");
                git.checkout().setCreateBranch(true).setName(branch).call();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    //获取某个用户的所有提交
    public static ArrayList GetAllCommit(Git git, String branch){
        try{
            //首先切回主分支，获得主分支的提交commitID
            BackToMaster(git);

            Iterable<RevCommit> logsMaster = git.log().call();
            String temp=null;
            for (RevCommit commit:logsMaster){
//                System.out.println(commit.getName());
                if (commit.getName()!=null){
                    temp=commit.getName();
                    break;
                }
            }
            System.out.println("temp\t"+temp);
            //将分支切到自己所在的branch
            git.checkout().setCreateBranch(false).setName(branch).call();
            ArrayList arrayList=new ArrayList();
            Iterable<RevCommit> logs = git.log().call();
            for(RevCommit commit : logs) {
                String commitID = commit.getName();
                if (!commitID.equals(temp)){
                    arrayList.add(commitID);
                    System.out.println(commitID);
                }
            }

            return arrayList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    //版本回退
    public static void VersionBack(Git git,String branch,String commitID){
        try{
            git.checkout().setCreateBranch(false).setName(commitID).call();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
