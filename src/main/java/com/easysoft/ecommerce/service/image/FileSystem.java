package com.easysoft.ecommerce.service.image;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FilenameFilter;

public class FileSystem {

    private File file;

    public FileSystem(String path, boolean createNew) {
        if (!StringUtils.isEmpty(path)) {
            file = new File(path);
        } else {
            throw new IllegalArgumentException("Path is not a folder: " + path);
        }
        if (createNew) {
            if (!file.exists()) {
                boolean success = file.mkdir();
                if (!success) throw new IllegalArgumentException("Cannot create folder because permission: " + path);
            }
        } else {
            if (!file.exists()) throw new IllegalArgumentException("Path doesn't exist: " + path);
        }

    }

    public String[] getFiles() {
        return file.list();
    }

    public String[] getFilesStartWith(final String prefix) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().startsWith(prefix.toLowerCase());
            }
        };
        return file.list(filter);
    }

    public String[] getFilesStartWith(final String[] prefix) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                for (String pre : prefix) {
                    if (name.toLowerCase().startsWith(pre)) {
                        return true;
                    }
                }
                return false;
            }
        };
        return file.list(filter);
    }

    public String[] getFilesEndWith(final String postfix) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(postfix.toLowerCase());
            }
        };
        return file.list(filter);
    }

    public String[] getFilesEndWith(final String[] postfix) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                for (String post : postfix) {
                    if (name.toLowerCase().endsWith(post)) {
                        return true;
                    }
                }
                return false;
            }
        };
        return file.list(filter);
    }

    public String[] getImages() {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".gif");
            }
        };
        return file.list(filter);
    }


    public String getPath() {
        return file.getAbsolutePath();
    }
}
