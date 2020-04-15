package com.aman.sharenotes;

public class UploadPdf {
    String date;
    String description;
    String File;
    String FileRandomKey ;
    String Filename;
    String time;

    public UploadPdf() {
    }

    public UploadPdf(String date, String description, String File, String FileRandomKey, String Filename, String time) {
        this.date = date;
        this.description = description;
        this.File = File;
        this.FileRandomKey = FileRandomKey;
        this.Filename = Filename;
        this.time = time;
    }


    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getFile() {
        return File;
    }

    public String getFileRandomKey() {
        return FileRandomKey;
    }

    public String getFilename() {
        return Filename;
    }

    public String getTime() {
        return time;
    }
}
