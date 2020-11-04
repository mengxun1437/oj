# OJ系统后端API

###### 导航：

<!-- TOC -->autoauto- [OJ系统后端API](#oj系统后端api)auto                    - [导航：](#导航)auto                    - [报错信息表：](#报错信息表)auto        - [一.教师](#一教师)auto            - [1.1教师注册](#11教师注册)auto                    - [注意：账号2-6位，密码6-20位](#注意账号2-6位密码6-20位)auto                    - [接口：[PUT]http://mengxun.online/api/oj/teacher/](#接口puthttpmengxunonlineapiojteacher)auto                    - [正确示例](#正确示例)auto                    - [返回结果](#返回结果)auto                    - [错误案例](#错误案例)auto                    - [返回结果](#返回结果-1)auto            - [1.2教师登录](#12教师登录)auto                    - [注意：账号2-6位，密码6-20位](#注意账号2-6位密码6-20位-1)auto                    - [接口：[POST]http://mengxun.online/api/oj/teacher/](#接口posthttpmengxunonlineapiojteacher)auto                    - [正确示例](#正确示例-1)auto                    - [返回结果](#返回结果-2)auto                    - [错误示例](#错误示例)auto                    - [返回结果](#返回结果-3)auto            - [1.3通过id获取教师的具体信息](#13通过id获取教师的具体信息)auto                    - [接口：[GET]http://mengxun.online/api/oj/teacher/{id}](#接口gethttpmengxunonlineapiojteacherid)auto                    - [返回结果[正确]](#返回结果正确)auto                    - [返回结果[错误]](#返回结果错误)auto            - [1.4修改教师的某些信息](#14修改教师的某些信息)auto                    - [注意：账号2-6位，密码6-20位，性别{0,1}，年龄[10,60]](#注意账号2-6位密码6-20位性别01年龄1060)auto                    - [接口：[PATCH]http://mengxun.online/api/oj/teacher/{id}](#接口patchhttpmengxunonlineapiojteacherid)auto                    - [正确示例：假设只需修改密码](#正确示例假设只需修改密码)auto                    - [返回结果](#返回结果-4)auto                    - [错误示例](#错误示例-1)auto                    - [返回结果](#返回结果-5)auto        - [二.学生](#二学生)auto            - [2.1学生注册](#21学生注册)auto                    - [注意：账号2-6位，密码6-20位](#注意账号2-6位密码6-20位-2)auto                    - [接口：[PUT]http://mengxun.online/api/oj/student/](#接口puthttpmengxunonlineapiojstudent)auto                    - [正确示例](#正确示例-2)auto                    - [返回结果](#返回结果-6)auto                    - [错误案例](#错误案例-1)auto                    - [返回结果](#返回结果-7)auto            - [2.2学生登录](#22学生登录)auto                    - [注意：账号2-6位，密码6-20位](#注意账号2-6位密码6-20位-3)auto                    - [接口：[POST]http://mengxun.online/api/oj/student/](#接口posthttpmengxunonlineapiojstudent)auto                    - [正确示例](#正确示例-3)auto                    - [返回结果](#返回结果-8)auto                    - [错误示例](#错误示例-2)auto                    - [返回结果](#返回结果-9)auto            - [2.3通过id获取学生的具体信息](#23通过id获取学生的具体信息)auto                    - [接口：[GET]http://mengxun.online/api/oj/student/{id}](#接口gethttpmengxunonlineapiojstudentid)auto                    - [返回结果[正确]](#返回结果正确-1)auto                    - [返回结果[错误]](#返回结果错误-1)auto            - [2.4修改学生的某些信息](#24修改学生的某些信息)auto                    - [注意：账号2-6位，密码6-20位](#注意账号2-6位密码6-20位-4)auto                    - [接口：[PATCH]http://mengxun.online/api/oj/student/{id}](#接口patchhttpmengxunonlineapiojstudentid)auto                    - [正确示例：假设只需修改密码](#正确示例假设只需修改密码-1)auto                    - [返回结果](#返回结果-10)auto                    - [错误示例](#错误示例-3)auto                    - [返回结果](#返回结果-11)autoauto<!-- /TOC -->

###### 报错信息表：

| 错误码(code) | 说明                                          |
| ------------ | --------------------------------------------- |
| 0            | success,如果有数据，看返回json数据中的”data"  |
| 40000        | 操作失败，具体原因看返回的"json"数据中的"msg" |



### 一.教师

#### 1.1教师注册

###### 注意：账号2-6位，密码6-20位

###### 接口：[PUT]http://mengxun.online/api/oj/teacher/

###### 正确示例

```json
{
    "name":"张三",
    "password":"zhangsan123456"
}
```

###### 返回结果

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

###### 错误案例

```json
{
    "name":"张三",
    "password":"zhangsan123456"
}
```

###### 返回结果

```json
{
    "code": 40000,
    "msg": "用户名已经注册",
    "data": null
}
```

#### 1.2教师登录

###### 注意：账号2-6位，密码6-20位

###### 接口：[POST]http://mengxun.online/api/oj/teacher/

###### 正确示例

```json
{
    "name":"张三",
    "password":"zhangsan123456"
}
```

###### 返回结果

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

###### 错误示例

```json
{
    "name":"张三",
    "password":"zhangsan12345"
}
```

###### 返回结果

```json
{
    "code": 40000,
    "msg": "教师密码错误",
    "data": null
}
```

#### 1.3通过id获取教师的具体信息

###### 接口：[GET]http://mengxun.online/api/oj/teacher/{id}

例：http://mengxun.online/api/oj/teacher/5322611b-3fb9-4c3d-adf9-af57f1fbe066

###### 返回结果[正确]

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

###### 返回结果[错误]

```json
{
    "code": 40000,
    "msg": "提供的教师id不存在",
    "data": null
}
```

#### 1.4修改教师的某些信息

###### 注意：账号2-6位，密码6-20位，性别{0,1}，年龄[10,60]

###### 接口：[PATCH]http://mengxun.online/api/oj/teacher/{id}

例： http://mengxun.online/api/oj/teacher/5322611b-3fb9-4c3d-adf9-af57f1fbe066 

*说明：需要修改哪些信息，就在json数据中传哪些信息*

###### 正确示例：假设只需修改密码

```json
{
    "password":"wangwujun"
}
```

###### 返回结果

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

###### 错误示例

```json
{
    "password":"wa"
}
```

###### 返回结果

```json
{
    "code": 40000,
    "msg": "用户密码长度错误",
    "data": null
}
```



### 二.学生

#### 2.1学生注册

###### 注意：账号2-6位，密码6-20位

###### 接口：[PUT]http://mengxun.online/api/oj/student/

###### 正确示例

```json
{
    "name":"李四",
    "password":"lisi123456"
}
```

###### 返回结果

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

###### 错误案例

```json
{
    "name":"李四",
    "password":"zhangsan123"
}
```

###### 返回结果

```json
{
    "code": 40000,
    "msg": "用户名已经注册",
    "data": null
}
```

#### 2.2学生登录

###### 注意：账号2-6位，密码6-20位

###### 接口：[POST]http://mengxun.online/api/oj/student/

###### 正确示例

```json
{
    "name":"李四",
    "password":"zhangsan123456"
}
```

###### 返回结果

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

###### 错误示例

```json
{
    "name":"李四",
    "password":"lisi1234"
}
```

###### 返回结果

```json
{
    "code": 40000,
    "msg": "用户密码错误",
    "data": null
}
```

#### 2.3通过id获取学生的具体信息

###### 接口：[GET]http://mengxun.online/api/oj/student/{id}

例：http://mengxun.online/api/oj/student/4a379f0a-28ae-4937-9fc4-c5366bb6cb76

###### 返回结果[正确]

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

###### 返回结果[错误]

```json
{
    "code": 40000,
    "msg": "提供的学生id不存在",
    "data": null
}
```

#### 2.4修改学生的某些信息

###### 注意：账号2-6位，密码6-20位

###### 接口：[PATCH]http://mengxun.online/api/oj/student/{id}

例： http://mengxun.online/api/oj/student/4a379f0a-28ae-4937-9fc4-c5366bb6cb76

*说明：需要修改哪些信息，就在json数据中传哪些信息*

###### 正确示例：假设只需修改密码

```json
{
    "password":"wangwujun"
}
```

###### 返回结果

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

###### 错误示例

```json
{
    "password":"wa"
}
```

###### 返回结果

```json
{
    "code": 40000,
    "msg": "用户密码长度错误",
    "data": null
}
```

