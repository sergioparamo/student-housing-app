package cat.itb.studenthousing.models;

import android.os.Parcelable;

import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

public class House implements Serializable {

    String houseId;
    String title;
    String ownerId;
    String description;
    String address;
    String area;
    String facilities;
    String picture;
    double deposit;
    double rent;

    public House() {
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public House(String houseId, String title, String ownerId, String description, String address, String area, String facilities, String picture, double deposit, double rent) {
        this.houseId = houseId;
        this.title = title;
        this.ownerId = ownerId;
        this.description = description;
        this.address = address;
        this.area = area;
        this.facilities = facilities;
        this.picture = picture;
        this.deposit = deposit;
        this.rent = rent;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    @Override
    public String toString() {
        return "House{" +
                "houseId='" + houseId + '\'' +
                ", title='" + title + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", area='" + area + '\'' +
                ", facilities=" + facilities +
                ", deposit=" + deposit +
                ", rent=" + rent +
                '}';
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }
}
