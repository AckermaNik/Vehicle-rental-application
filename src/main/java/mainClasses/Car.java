/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

/**
 *
 * @author 30697
 */
public class Car extends Vehicle {
    String car_type;
    int passenger_num;


    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public int getPassenger_num() {
        return passenger_num;
    }

    public void setPassenger_num(int pass_num) {
        this.passenger_num = pass_num;
    }

    public String ToString() {
        return "Reg: " + getReg_no() + " Car_type: " + car_type + "Km: " + getKm() + "Colour: " + getColour() + "Model: " + getModel() + "Brand: " + getBrand()
                + "Passenger Number: " + passenger_num + "Daily rent cost" + getDaily_rent_cost() + "Daily_insurance cost" + getDaily_insurance_cost() + "\n";

    }
}
