package com.example.apprpg.models;

public class VersionControl {
    private int versionNumber;
    private String newestVersionLink;
    private boolean allowOutDatedLogin;

    public VersionControl() {
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public String getNewestVersionLink() {
        return newestVersionLink;
    }

    public boolean getAllowOutDatedLogin() {
        return allowOutDatedLogin;
    }
}
