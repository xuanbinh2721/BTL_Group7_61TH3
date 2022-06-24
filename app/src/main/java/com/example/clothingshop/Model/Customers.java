package com.example.clothingshop.Model;

public class Customers {
    private String Name,phone,password,address,profilePic;
    // constructor without paramaters
    public Customers(){
    }
    public Customers(String Name, String phone, String password, String address, String profilePic) {
        this.Name = Name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.profilePic = profilePic;
    }
    public String getName() {
        return Name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setfName(String fName) {
        this.Name = Name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

}


