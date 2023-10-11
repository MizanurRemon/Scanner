package com.scanner.scanner.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileListResponse {
    @SerializedName("invoiceNo")
    @Expose
    public String invoiceNo;
    @SerializedName("invoiceDate")
    @Expose
    public String invoiceDate;
    @SerializedName("fileName")
    @Expose
    public String fileName;
    @SerializedName("fileType")
    @Expose
    public String fileType;
    @SerializedName("file")
    @Expose
    public String file;
}
