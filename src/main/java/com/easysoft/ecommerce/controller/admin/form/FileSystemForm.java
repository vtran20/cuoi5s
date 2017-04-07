package com.easysoft.ecommerce.controller.admin.form;

public class FileSystemForm {

    private String path;
    private String[] start;
    private String[] end;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getStart() {
        return start;
    }

    public void setStart(String[] start) {
        this.start = start;
    }

    public String[] getEnd() {
        return end;
    }

    public void setEnd(String[] end) {
        this.end = end;
    }
}
