package cat.itb.studenthousing.models;

import java.io.Serializable;


public class Student implements Serializable {

    String studentId;
    String email;
    String name;
    String phoneNumber;

    public Student() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Student(String studentId, String email, String name, String phoneNumber) {
        this.studentId = studentId;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
