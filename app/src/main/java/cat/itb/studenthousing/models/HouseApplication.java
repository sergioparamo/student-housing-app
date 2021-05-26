package cat.itb.studenthousing.models;

import org.w3c.dom.Text;

import java.io.Serializable;

public class HouseApplication implements Serializable {

    String applicationId;
    String studentName;
    String studentEmail;
    String houseId;
    String studentId;
    String state;

    public HouseApplication() {
    }

    public HouseApplication(String applicationId, String studentName, String studentEmail, String houseId, String studentId, String state) {
        this.applicationId = applicationId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.houseId = houseId;
        this.studentId = studentId;
        this.state = state;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "HouseApplication{" +
                "applicationId='" + applicationId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                ", houseId='" + houseId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
