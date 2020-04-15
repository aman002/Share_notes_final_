package com.aman.sharenotes;

public class adminlist {
    String Name;
    String Password;
    String Number;

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getPassword() {
        return Password;
    }

    public adminlist() {
    }

    public adminlist(String Name) {
        Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        Name = Name;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
