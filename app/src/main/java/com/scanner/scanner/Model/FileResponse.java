package com.scanner.scanner.Model;

import com.google.gson.annotations.SerializedName;

public class FileResponse {
    @SerializedName("fileName")
    private String fileName;
    @SerializedName("file")
    private String fileString;
    @SerializedName("fileType")
    private String fileExtension;

    public FileResponse(String fileName, String fileString, String fileExtension) {
        this.fileName = fileName;
        this.fileString = fileString;
        this.fileExtension = fileExtension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileString() {
        return fileString;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
