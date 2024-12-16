package com.example.fruit.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;


/**
 * 用户
 */
public class User extends DataSupport implements Serializable {
    private String account;//账号
    private String password;//密码
    private String nickName;//昵称
    private String age;//年龄
    private String email;//邮箱

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String account, String password, String nickName, String age, String email) {
        this.account = account;
        this.password = password;
        this.nickName = nickName;
        this.age = age;
        this.email = email;
    }
}
