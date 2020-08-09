package com.shawinfosolutions.bankcustomerapp.Model;

public class CustomerDetails {

private String aadharNo,firstname,id,lastname,mailId,mobilenumber;


    public CustomerDetails() {
    }

    public CustomerDetails(String id,String firstname, String lastname, String mailId,String aadharNo, String mobilenumber) {
        this.aadharNo = aadharNo;
        this.firstname = firstname;
        this.id = id;
        this.lastname = lastname;
        this.mailId = mailId;
        this.mobilenumber = mobilenumber;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }
}
