package com.winision.velammalitapp;

/**
 * Created by Ashwin on 21-01-2018.
 */

public class Members {
    String name;
    String status;
    String downloadUrl;

    public Members() {

    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Members(String name, String status, String downloadUrl) {
        this.name = name;
        this.status = status;
        this.downloadUrl = downloadUrl;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
