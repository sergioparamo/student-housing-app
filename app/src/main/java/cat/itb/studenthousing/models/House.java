package cat.itb.studenthousing.models;

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
    int picture;
    double deposit;
    double rent;

    public House() {
    }

    public int getPictures() {
        return picture;
    }

    public void setPictures(int pictures) {
        this.picture = pictures;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public House(String title, String description, String address, String area, String  facilities, int  pictures , double deposit, double rent) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.facilities = facilities;
        this.deposit = deposit;
        this.rent = rent;
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

    public  String getFacilities() {
        return facilities;
    }

    public void setFacilities( String  facilities) {
        this.facilities = facilities;
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
