/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

/**
 *
 * @author 30697
 */
public class Rent {
    String username;
    String f_name, reg_no, rent_date, rent_hour, return_date, return_hour, driver_fname, driver_lname;
    Double total_cost, insurance_cost;
    int valid_bit;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(Double total_cost) {
        this.total_cost = total_cost;
    }

    public int getValid_bit() {
        return valid_bit;
    }

    public void setValid_bit(int valid_bit) {
        this.valid_bit = valid_bit;
    }

    public Double getInsurance_cost() {
        return insurance_cost;
    }

    public void setInsurance_cost(Double total_cost) {
        this.insurance_cost = insurance_cost;
    }

    public String getDriver_fname() {
        return driver_fname;
    }

    public void setDriver_fname(String driver_fname) {
        this.driver_fname = driver_fname;
    }

    public String getDriver_lname() {
        return driver_lname;
    }

    public void setDriver_lname(String driver_lname) {
        this.driver_lname = driver_lname;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getRent_date() {
        return rent_date;
    }

    public void setRent_date(String rent_date) {
        this.rent_date = rent_date;
    }

    public String getRent_hour() {
        return rent_hour;
    }

    public void setRent_hour(String rent_hour) {
        this.rent_hour = rent_hour;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getReturn_hour() {
        return return_hour;
    }

    public void setReturn_hour(String return_hour) {
        this.return_hour = return_hour;
    }
}
