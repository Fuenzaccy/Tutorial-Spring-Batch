package com.ccsw.tutorial_batch.model;

public class GameExport {

    private String title;
    private String availability;

    public GameExport() {
    }

    public GameExport(String title, String availability) {
        this.title = title;
        this.availability = availability;
    }

    public String getTitle() {
        return title;
    }

    public void serTitle(String title) {
        this.title = title;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    @Override
    public String toString() {
        return "GameExport [title=" + title + ", availability=" + availability + "]";
    }
}
