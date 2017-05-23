package com.simbiotic.privalia.model.entities;

import java.io.Serializable;

/**
 * Created by Rayworld on 18/05/2017.
 */

public class Movie implements Serializable {

    private String id;
    private String title;
    private String year;
    private String resume;
    private String photo;

    public Movie () {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
