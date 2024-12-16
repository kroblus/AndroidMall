package com.example.fruit.bean;

import org.litepal.crud.DataSupport;


/**
 * 浏览记录
 */
public class Browse extends DataSupport {
    private String account;//账号
    private String title;//水果标题

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Browse(String account, String title) {
        this.account = account;
        this.title = title;
    }
}
