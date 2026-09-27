/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainClasses;

/**
 *
 * @author dimit
 */
public class StatPack {
    private int max_rent_duration;
    private int min_rent_duration;
    private int avg_rent_duration;

    public void setMaxRentDuration(int max_rent_duration) {
        this.max_rent_duration = max_rent_duration;
    }

    public void setMinRentDuration(int min_rent_duration) {
        this.min_rent_duration = min_rent_duration;
    }

    public void setAvgRentDuration(int avg_rent_duration) {
        this.avg_rent_duration = avg_rent_duration;
    }

    public int getMaxRentDuration() {
        return max_rent_duration;
    }

    public int getMinRentDuration() {
        return min_rent_duration;
    }

    public int getAvgRentDuration() {
        return avg_rent_duration;
    }
}
