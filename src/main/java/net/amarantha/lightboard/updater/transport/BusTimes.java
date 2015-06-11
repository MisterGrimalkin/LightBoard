package net.amarantha.lightboard.updater.transport;

import java.util.ArrayList;
import java.util.List;

public class BusTimes {

    private int stopCode;
    private String busNumber;
    private String displayAs;
    private List<Integer> dueTimes = new ArrayList<>();

    public BusTimes(int stopCode, String busNumber, String displayAs) {
        this.stopCode = stopCode;
        this.busNumber = busNumber;
        this.displayAs = displayAs;
    }



}
