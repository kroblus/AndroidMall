package com.example.fruit.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Orders extends DataSupport implements Serializable {
    private String account;//账号
    private String title;//标题
    private String number;//编号
    private String issuer;//价格
    private String img;//图片
    private String amount;//数量
    private String date;//时间

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
      this.issuer=issuer;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Orders(String issuer,String account,String title, String number, String amount, String date) {
        this.issuer=issuer;
        this.account = account;
        this.title = title;
        this.number = number;
        this.amount = amount;
        this.date = date;

    }
}
