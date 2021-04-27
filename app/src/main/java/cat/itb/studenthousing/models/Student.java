package cat.itb.studenthousing.models;

import java.io.Serializable;


public class Student implements Serializable {

    String studentId;
    String username;
    String email;
    String name;
    String password;
    int socialSecurityNumber;
    int mobile;

    public Student(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Student(String username, String name, String email, String password, int socialSecurityNumber, int mobile) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.socialSecurityNumber = socialSecurityNumber;
        this.mobile = mobile;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(int socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }
}
