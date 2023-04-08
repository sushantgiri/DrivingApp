package com.driving_app.model;

public class Instructor {

    private String name;
    private String rating;
    private String drivingExperienceDetails;
    private String profileUrl;

    private String instructorAvailability;

    public String getInstructorAvailability() {
        return instructorAvailability;
    }

    public void setInstructorAvailability(String instructorAvailability) {
        this.instructorAvailability = instructorAvailability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDrivingExperienceDetails() {
        return drivingExperienceDetails;
    }

    public void setDrivingExperienceDetails(String drivingExperienceDetails) {
        this.drivingExperienceDetails = drivingExperienceDetails;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
