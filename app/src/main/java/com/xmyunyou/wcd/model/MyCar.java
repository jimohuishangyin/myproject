package com.xmyunyou.wcd.model;

import java.io.Serializable;

/**
 * Created by 95 on 2015/4/14.
 */
public class MyCar implements Serializable {

    public int ID;
    public String Name;
    public int CarManufacturerID;
    public int CarModelID;
    public String ImageUrl;
    public String ZiMu;
    public int OneHundredSeconds;
    public CarManufacturer CarManufacturer;
    public CarModel CarModel;
    public String EngineName;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCarManufacturerID() {
        return CarManufacturerID;
    }

    public void setCarManufacturerID(int carManufacturerID) {
        CarManufacturerID = carManufacturerID;
    }

    public int getCarModelID() {
        return CarModelID;
    }

    public void setCarModelID(int carModelID) {
        CarModelID = carModelID;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getZiMu() {
        return ZiMu;
    }

    public void setZiMu(String ziMu) {
        ZiMu = ziMu;
    }

    public int getOneHundredSeconds() {
        return OneHundredSeconds;
    }

    public void setOneHundredSeconds(int oneHundredSeconds) {
        OneHundredSeconds = oneHundredSeconds;
    }

    public CarManufacturer getCarManufacturer() {
        return CarManufacturer;
    }

    public void setCarManufacturer(CarManufacturer carManufacturer) {
        CarManufacturer = carManufacturer;
    }

    public CarModel getCarModel() {
        return CarModel;
    }

    public void setCarModel(CarModel carModel) {
        CarModel = carModel;
    }

    public String getEngineName() {
        return EngineName;
    }

    public void setEngineName(String engineName) {
        EngineName = engineName;
    }
}


