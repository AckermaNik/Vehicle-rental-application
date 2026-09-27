/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

/**
 *
 * @author 30697
 */
public class Vehicle {
    String reg_no, colour, model, brand, type;
    int count = 0; //how many times each vehicle is rented
    Double km, daily_rent_cost, daily_insurance_cost;

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_num) {
        this.reg_no = reg_no;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount() {
        this.count++;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public Double getDaily_rent_cost() {
        return daily_rent_cost;
    }

    public void setDaily_rent_cost(Double daily_rent_cost) {
        this.daily_rent_cost = daily_rent_cost;
    }

    public Double getDaily_insurance_cost() {
        return daily_insurance_cost;
    }

    public void setDaily_insurance_cost(Double daily_insurance_cost) {
        this.daily_insurance_cost = daily_insurance_cost;
    }

}
