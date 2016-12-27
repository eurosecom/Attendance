package com.eusecom.attendance.models;

public class Contact {
private String contactProfession, contactName, contactAddress;
private int contactNumber, contactStatus;

public Contact(){
}

public String getContactProfession(){
    return contactProfession;
}
public void setContactProfession(String contactProfession){
    this.contactProfession = contactProfession;
}


public String getContactName(){
    return contactName;
}
public void setContactName(String contactName){
    this.contactName = contactName;
}


public String getContactAddress(){
    return contactAddress;
}
public void setContactAddress(String contactAddress){
    this.contactAddress = contactAddress;
}


public int getContactNumber(){
    return contactNumber;
}
public void setContactNumber(int contactNumber){
    this.contactNumber = contactNumber;
}


public int getContactStatus(){
    return contactStatus;
}
public void setContactStatus(int contactStatus){
    this.contactStatus = contactStatus;
}


}