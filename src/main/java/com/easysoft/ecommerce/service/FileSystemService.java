package com.easysoft.ecommerce.service;

import java.io.File;


public interface FileSystemService {

    /**
     * Saves a file to application working folder.
     * @param sourceFile source file
     * @param targetRelativePath relative target path
     * @return created file
     * @throws Exception
     */
    File save(File sourceFile, String targetRelativePath) throws Exception;

    /**
     * Finds a File in the application working folder
     * @param relativePath relative file path
     * @return the file
     * @throws Exception
     */
    File find(String relativePath) throws Exception;

    /**
     * Download files in database to application working folder (on local file system). Will be executed automatically
     * every 30 seconds.
     * @throws Exception
     */
    void download() throws Exception;
}