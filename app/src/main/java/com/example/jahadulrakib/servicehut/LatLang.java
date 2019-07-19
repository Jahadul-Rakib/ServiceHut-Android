package com.example.jahadulrakib.servicehut;

public class LatLang {
    private static double latiture;
    private static double longiture;

    public LatLang() {
    }

    public LatLang(double latiture, double longiture) {
        this.latiture = latiture;
        this.longiture = longiture;
    }

    public double getLatiture() {
        return latiture;
    }

    public void setLatiture(double latiture) {
        this.latiture = latiture;
    }

    public double getLongiture() {
        return longiture;
    }

    public void setLongiture(double longiture) {
        this.longiture = longiture;
    }
}
