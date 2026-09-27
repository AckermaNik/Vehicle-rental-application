/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author 30697
 */
public class Service {

    String reg_no, import_date;
    int duration; //in days
    Double cost;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getImport_date() {
        return import_date;
    }

    public void setImport_date(String import_date) {
        this.import_date = import_date;
    }

    /**
     * @param current_date The date to base the check.
     * @return Whether the vehicle is active at the provided date or not.
     */
    public boolean isActive(String current_date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate in_date = LocalDate.parse(import_date, formatter);
        LocalDate out_date = LocalDate.parse(current_date, formatter);

        return (out_date.compareTo(in_date) > 0);
    }
}
