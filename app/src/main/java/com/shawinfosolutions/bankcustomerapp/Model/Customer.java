package com.shawinfosolutions.bankcustomerapp.Model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Customer {
    public String id;
    public String mobileNo;

    public String loginStatus;

    public Customer() {
    }

    public String getId() {
        return id;
    }

    public Customer(String id, String mobileNo, String loginStatus) {
        this.id=id;
        this.mobileNo = mobileNo;
        this.loginStatus = loginStatus;
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

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }
}
