package com.example.zhang.inkspill;

/**
 * Created by zhang on 2016/12/22.
 */

public class Updateinfo {
    private String version;
    private String description;
    private String downloadUrl;

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
