package com.appdynamics.extensions.apptweak.config;

/**
 * Created by mariomann on 23/08/16.
 */
public class MobileApplication {
    private String country;
    private String language;
    private String religion;
    private String applicationId;

    public String getCountry() {
        return country;
    }

    public String getLanguage() { return language; }

    public String getReligion() {
        return religion;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
