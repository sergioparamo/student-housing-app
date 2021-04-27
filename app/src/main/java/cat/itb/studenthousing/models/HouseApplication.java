package cat.itb.studenthousing.models;

import org.w3c.dom.Text;

import java.io.Serializable;

public class HouseApplication implements Serializable {

    String applicationId;
    String houseId;
    String studentId;
    String state;

    public HouseApplication() {
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

    public HouseApplication(String applicationId, String houseId, String studentId, String state) {
        this.applicationId = applicationId;
        this.houseId = houseId;
        this.studentId = studentId;
        this.state = state;
    }
}
