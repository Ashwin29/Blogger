package com.winision.velammalitapp;

/**
 * Created by Ashwin on 14-01-2018.
 */

public class Posts {

    String title;
    String description;
    String name;
    String department;
    String downloadUrl;

    public Posts() {

    }

    public Posts(String title, String description, String name, String department, String downloadUrl) {
        this.title = title;
        this.description = description;
        this.name = name;
        this.department = department;
        this.downloadUrl = downloadUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDepartment() {
        return department;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setName(String name) {
        this.name = name;
    }
}
