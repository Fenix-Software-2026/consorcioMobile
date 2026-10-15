package com.ispc.consorciomobile.model;

import com.google.gson.annotations.SerializedName;

public class CambioContrasenaRequest {
    @SerializedName("current_password")
    private String currentPassword;

    @SerializedName("new_password")
    private String newPassword;

    public CambioContrasenaRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
