# OJ系统后端API

- [OJ系统后端API](#oj系统后端api)
          - [报错信息表](#报错信息表)
    - [一.教师](#一教师)
      - [1.1教师注册](#11教师注册)
          - [注意](#注意)
      - [1.2教师登录](#12教师登录)
          - [注意](#注意-1)
          - [接口](#接口)
      - [1.3通过id获取教师的具体信息](#13通过id获取教师的具体信息)
          - [接口](#接口-1)
      - [1.4修改教师的某些信息](#14修改教师的某些信息)
          - [注意](#注意-2)
          - [接口](#接口-2)
    - [二.学生](#二学生)
      - [2.1学生注册](#21学生注册)
          - [注意](#注意-3)
          - [接口](#接口-3)
      - [2.2学生登录](#22学生登录)
          - [注意](#注意-4)
          - [接口](#接口-4)
      - [2.3通过id获取学生的具体信息](#23通过id获取学生的具体信息)
          - [接口](#接口-5)
      - [2.4修改学生的某些信息](#24修改学生的某些信息)
          - [注意](#注意-5)
          - [接口](#接口-6)
    - [三.题目](#三题目)
      - [3.1增加一个新的题目](#31增加一个新的题目)
          - [注意](#注意-6)
          - [接口](#接口-7)
      - [3.2查看某个题目的具体信息](#32查看某个题目的具体信息)
          - [注意](#注意-7)
          - [接口](#接口-8)
      - [3.3修改题目的某些信息](#33修改题目的某些信息)
          - [注意](#注意-8)
          - [接口](#接口-9)
      - [3.4删除某一题目](#34删除某一题目)
          - [注意](#注意-9)
          - [接口](#接口-10)
      - [3.5获取题目列表](#35获取题目列表)
          - [注意](#注意-10)
          - [接口](#接口-11)

###### 报错信息表

| 错误码(code) | 说明                                          |
| ------------ | --------------------------------------------- |
| 0            | success,如果有数据，看返回json数据中的”data"  |
| 40000        | 操作失败，具体原因看返回的"json"数据中的"msg" |



### 一.教师

#### 1.1教师注册

###### 注意

账号2-6位，密码6-20位

接口

[PUT]http://mengxun.online/api/oj/teacher/

正确示例

```json
{
    "name":"张三",
    "password":"zhangsan123456"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "教师注册成功",
    "data": {
        "UpdateAt": "2020-10-31T16:44:28.624+0000",
        "CreateAt": "2020-10-31T16:44:28.624+0000",
        "ID": "9d790ae2-034e-481e-853c-817d023f2986",
        "Name": "张三"
    }
}
```

错误案例

```json
{
    "name":"张三",
    "password":"zhangsan123456"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "用户名已经注册",
    "data": null
}
```

#### 1.2教师登录

###### 注意

账号2-6位，密码6-20位

###### 接口

[POST]http://mengxun.online/api/oj/teacher/

正确示例

```json
{
    "name":"张三",
    "password":"zhangsan123456"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "教师存在，登录成功",
    "data": {
        "UpdateAt": "2020-10-31T16:44:28.624+0000",
        "CreateAt": "2020-10-31T16:44:28.624+0000",
        "ID": "9d790ae2-034e-481e-853c-817d023f2986",
        "Name": "张三"
    }
}
```

错误示例

```json
{
    "name":"张三",
    "password":"zhangsan12345"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "教师密码错误",
    "data": null
}
```

#### 1.3通过id获取教师的具体信息

###### 接口

[GET]http://mengxun.online/api/oj/teacher/{id}

例

http://mengxun.online/api/oj/teacher/5322611b-3fb9-4c3d-adf9-af57f1fbe066

返回结果[正确]

```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "Email": null,
        "UpdateAt": "2020-11-04T06:48:42.000+0000",
        "CreateAt": "2020-11-04T06:48:42.000+0000",
        "Sex": null,
        "ID": "5322611b-3fb9-4c3d-adf9-af57f1fbe066",
        "Age": null,
        "Name": "wang"
    }
}
```

返回结果[错误]

```json
{
    "code": 40000,
    "msg": "提供的教师id不存在",
    "data": null
}
```

#### 1.4修改教师的某些信息

###### 注意

​	1.账号2-6位，密码6-20位，性别{0,1}，年龄[10,60]

​	2.需要修改哪些信息，就在json数据中传哪些信息

###### 接口

[PATCH]http://mengxun.online/api/oj/teacher/{id}

例

 http://mengxun.online/api/oj/teacher/5322611b-3fb9-4c3d-adf9-af57f1fbe066 

正确示例：假设只需修改密码

```json
{
    "password":"wangwujun"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "教师信息修改成功",
    "data": {
        "Email": null,
        "UpdateAt": "2020-11-04T06:56:14.423+0000",
        "CreateAt": "2020-11-04T06:48:42.000+0000",
        "Sex": 0,
        "ID": "5322611b-3fb9-4c3d-adf9-af57f1fbe066",
        "Age": 18,
        "Name": "wujun1"
    }
}
```

错误示例

```json
{
    "password":"wa"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "用户密码长度错误",
    "data": null
}
```



### 二.学生

#### 2.1学生注册

###### 注意

账号2-6位，密码6-20位

###### 接口

[PUT]http://mengxun.online/api/oj/student/

正确示例

```json
{
    "name":"李四",
    "password":"lisi123456"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "学生注册成功",
    "data":{
        "UpdateAt": "2020-10-31T16:44:28.624+0000",
        "CreateAt": "2020-10-31T16:44:28.624+0000",
        "ID": "9d790ae2-034e-481e-853c-817d023f2986",
        "Name": "李四"
    }
}
```

错误案例

```json
{
    "name":"李四",
    "password":"zhangsan123"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "用户名已经注册",
    "data": null
}
```

#### 2.2学生登录

###### 注意

账号2-6位，密码6-20位

###### 接口

[POST]http://mengxun.online/api/oj/student/

正确示例

```json
{
    "name":"李四",
    "password":"zhangsan123456"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "学生存在，登录成功",
    "data": {
        "UpdateAt": "2020-10-31T16:44:28.624+0000",
        "CreateAt": "2020-10-31T16:44:28.624+0000",
        "ID": "9d790ae2-034e-481e-853c-817d023f2986",
        "Name": "李四"
    }
}
```

错误示例

```json
{
    "name":"李四",
    "password":"lisi1234"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "用户密码错误",
    "data": null
}
```

#### 2.3通过id获取学生的具体信息

###### 接口

[GET]http://mengxun.online/api/oj/student/{id}

例

http://mengxun.online/api/oj/student/4a379f0a-28ae-4937-9fc4-c5366bb6cb76

返回结果[正确]

```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "Email": null,
        "UpdateAt": "2020-11-04T06:59:42.000+0000",
        "CreateAt": "2020-11-04T06:59:42.000+0000",
        "ID": "4a379f0a-28ae-4937-9fc4-c5366bb6cb76",
        "Name": "hahaha"
    }
}
```

返回结果[错误]

```json
{
    "code": 40000,
    "msg": "提供的学生id不存在",
    "data": null
}
```

#### 2.4修改学生的某些信息

###### 注意

​	1.账号2-6位，密码6-20位

​	2.需要修改哪些信息，就在json数据中传哪些信息

###### 接口

[PATCH]http://mengxun.online/api/oj/student/{id}

例

http://mengxun.online/api/oj/student/4a379f0a-28ae-4937-9fc4-c5366bb6cb76

正确示例：假设只需修改密码

```json
{
    "password":"wangwujun"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "学生信息修改成功",
    "data": {
        "Email": null,
        "UpdateAt": "2020-11-04T07:01:45.932+0000",
        "CreateAt": "2020-11-04T06:59:42.000+0000",
        "ID": "4a379f0a-28ae-4937-9fc4-c5366bb6cb76",
        "Name": "hahaha"
    }
}
```

错误示例

```json
{
    "password":"wa"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "用户密码长度错误",
    "data": null
}
```



### 三.题目

#### 3.1增加一个新的题目

###### 注意

​	1.提交参数的json中必须由name/detail/creator/diff这四个参数

​	2.如果由代码样例需要code（base64形式）,提交code的时候必须加入codetype(代码文件类型)

​	3.diff是题目难度，可选参数0/1/2，整数形式提交

###### 接口

[PUT]http://mengxun.online/api/oj/question/

正确示例

```json
{
    "name":"题目一",
    "detail":"题目一的描述",
    "creator":"df7e5a0c-8920-45a2-b92a-604d66e95e48",
    "diff":2,
 "code":"ewogICAgIm5hbWUiOiLnjovmrabkv4oiLAogICAgInBhc3N3b3JkIjoid2FuZ3d1anVuIgp9Cg==",
    "codetype":"json"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "保存成功",
    "data": {
        "UpdateAt": "2020-11-05T12:11:45.567+0000",
        "TestData": "",
        "CreateAt": "2020-11-05T12:11:45.567+0000",
        "Diff": 2,
        "ID": "f7a6878b-7265-4a04-8095-4629328a2159",
        "Creator": "df7e5a0c-8920-45a2-b92a-604d66e95e48",
        "Detail": "题目一的描述",
        "Name": "题目一"
    }
}
```

错误案例

```json
{
    "name":"题目一",
    "detail":"题目一的描述",
    "creator":"df7e5a0c-8920-45a2-b92a-604d66e95e48",
    "diff":2,
    "code":"ewogICAgIm5hbWUiOiLnjovmrabkv4oiLAogICAgInBhc3N3b3JkIjoid2FuZ3d1anVuIgp9Cg=="
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "codetype参数缺失",
    "data": null
}
```

#### 3.2查看某个题目的具体信息

###### 注意

​	1.必须指定请求者的身份teacher/student

​	2.如果需要获取这一题的代码样例，需要指定获取代码样例的文件类型（codetype)，不需要的话可以不写

###### 接口

[POST]http://mengxun.online/api/oj/question/{id}

例

 http://mengxun.online/api/oj/question/f7a6878b-7265-4a04-8095-4629328a2159

正确案例

```json
{
   "identity":"student",
   "codetype":"json"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "题目存在",
    "data": {
        "UpdateAt": "2020-11-05T12:11:45.000+0000",
        "code": "ewogICAgIm5hbWUiOiLnjovmrabkv4oiLAogICAgInBhc3N3b3JkIjoid2FuZ3d1anVuIgp9Cg==",
        "CreateAt": "2020-11-05T12:11:45.000+0000",
        "Diff": 2,
        "codetype": "json",
        "ID": "f7a6878b-7265-4a04-8095-4629328a2159",
        "Creator": "df7e5a0c-8920-45a2-b92a-604d66e95e48",
        "Detail": "题目一的描述",
        "Name": "题目一"
    }
}
```

错误案例

```json
{
   "codetype":"json"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "必须说明用户身份：teacher/student",
    "data": null
}
```

#### 3.3修改题目的某些信息

###### 注意

​	1.必须提交教师的id（json中的creator）

​	2.需要修改哪些信息就在json中写哪些信息，如果需要添加codetype不同的代码样例，也可以通过这个接口

###### 接口

[PATCH]http://mengxun.online/api/oj/question/{id}

例

 http://mengxun.online/api/oj/question/f7a6878b-7265-4a04-8095-4629328a2159

正确案例

```json
{
    "creator":"df7e5a0c-8920-45a2-b92a-604d66e95e48",
    "name":"题目更改后"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "修改题目信息成功",
    "data": null
}
```

错误案例

```json
{
    "name":"题目更改后"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "creator参数缺失",
    "data": null
}
```

#### 3.4删除某一题目

###### 注意

​	1.必须提交教师的id(json中的creator)

​	2.删除的同时会删除教师关于这道题发布的所有类型的代码案例

###### 接口

[DELETE]http://mengxun.online/api/oj/question/{id}

例

 http://mengxun.online/api/oj/question/b5aee93a-d47a-479a-adb6-a1e625fd7e8e 

正确案例

```json
{
    "creator":"df7e5a0c-8920-45a2-b92a-604d66e95e48" 
}
```

返回结果

```json
{
    "code": 0,
    "msg": "删除题目成功",
    "data": null
}
```

错误案例

```json
{
    "creator":"df7e5a0c-8920-45a2-b92a-604d66e9" 
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "用户校验失败",
    "data": null
}
```

#### 3.5获取题目列表

###### 注意

​	1.需要指定获取列表者的身份（identity:teacher/student)

​	2.如果指定的身份是teacher,需要指定creator(教师获取自己发布的题目时候用),如果是学生，则不需要，通过接口获得的是所有的题目列表

###### 接口

[POST]http://mengxun.online/api/oj/quesition/list

正确案例

```json
{
   "identity":"teacher",
   "creator":"df7e5a0c-8920-45a2-b92a-604d66e95e48"
}
```

返回结果

```json
{
    "code": 0,
    "msg": "success",
    "data": [
        {
            "UpdateAt": "2020-11-05T11:58:18.000+0000",
            "CreateAt": "2020-11-05T11:58:18.000+0000",
            "Diff": 2,
            "ID": "3cc5e621-94f9-45be-beb0-61c503c7a8e8",
            "Name": "json"
        },
        {
            "UpdateAt": "2020-11-05T11:59:05.000+0000",
            "CreateAt": "2020-11-05T11:59:05.000+0000",
            "Diff": 2,
            "ID": "aaf9567e-2a16-4956-a37c-93995f1660de",
            "Name": "json"
        },
        {
            "UpdateAt": "2020-11-05T12:11:45.000+0000",
            "CreateAt": "2020-11-05T12:11:45.000+0000",
            "Diff": 2,
            "ID": "f7a6878b-7265-4a04-8095-4629328a2159",
            "Name": "题目一"
        },
        {
            "UpdateAt": "2020-11-05T12:16:48.000+0000",
            "CreateAt": "2020-11-05T12:16:48.000+0000",
            "Diff": 2,
            "ID": "588fb8be-ddb5-4a27-861d-e8226051625b",
            "Name": "题目一"
        },
        {
            "UpdateAt": "2020-11-09T08:00:03.000+0000",
            "CreateAt": "2020-11-09T07:57:07.000+0000",
            "Diff": 2,
            "ID": "b7783a75-6765-4fd8-8c4a-f29e5149583c",
            "Name": "题目更改后"
        },
        {
            "UpdateAt": "2020-11-11T04:22:07.000+0000",
            "CreateAt": "2020-11-11T04:22:07.000+0000",
            "Diff": 2,
            "ID": "228f685e-d1b1-43fa-88bf-b37b022a6747",
            "Name": "题目一"
        }
    ]
}
```

错误案例

```json
{
   "identity":"teacher"
}
```

返回结果

```json
{
    "code": 40000,
    "msg": "教师身份查询需指明creator",
    "data": null
}
```

