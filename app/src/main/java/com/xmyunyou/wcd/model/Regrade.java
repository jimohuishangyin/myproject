package com.xmyunyou.wcd.model;

/**
 * Created by sanmee on 2014/12/18.
 */
public class Regrade {

    private String UpdateInfo;
    private Boolean Forced;
    private String NewVerDownload;
    private String VersionCode;

    public String getUpdateInfo() {
        return UpdateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        UpdateInfo = updateInfo;
    }

    public Boolean getForced() {
        return Forced;
    }

    public void setForced(Boolean forced) {
        Forced = forced;
    }

    public String getNewVerDownload() {
        return NewVerDownload;
    }

    public void setNewVerDownload(String newVerDownload) {
        NewVerDownload = newVerDownload;
    }

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String versionCode) {
        VersionCode = versionCode;
    }
}
