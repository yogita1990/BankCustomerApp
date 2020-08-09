package com.shawinfosolutions.bankcustomerapp.Model;

public class CustomerTokens {

    private String id,mobileNo,token;


    public CustomerTokens(String id, String mobileNo, String token) {
        this.id = id;
        this.mobileNo = mobileNo;
        this.token = token;
    }

    public CustomerTokens() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
