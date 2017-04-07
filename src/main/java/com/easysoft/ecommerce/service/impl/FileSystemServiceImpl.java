package com.easysoft.ecommerce.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.easysoft.ecommerce.dao.FileObjectDao;
import com.easysoft.ecommerce.model.FileObject;
import com.easysoft.ecommerce.service.FileSystemService;

@Service
@Transactional
public class FileSystemServiceImpl implements FileSystemService {

    private FileObjectDao fileObjectDao;
    private File workingDir;

    @Autowired
    public FileSystemServiceImpl(FileObjectDao fileObjectDao,
            @Value("#{configurationService.workingDir}") File workingDir) {
        this.fileObjectDao   = fileObjectDao;
        this.workingDir      = workingDir;
    }

    @Override
    public File save(File sourceFile, String targetRelativePath) throws Exception {

        // validate input to make sure the file is under workingDir to avoid security issues
        String modifiedPath = targetRelativePath;
        while (modifiedPath.startsWith("/")) {
            modifiedPath = modifiedPath.substring(1);
        }
        File destFile = new File(this.workingDir, modifiedPath);
        String workingPath = this.workingDir.getCanonicalPath();
        String filePath = destFile.getCanonicalPath();
        while (destFile != null && !destFile.equals(this.workingDir)) {
            destFile = destFile.getParentFile();
        }
        if (destFile == null || !filePath.startsWith(workingPath)) {
            throw new IllegalArgumentException("Invalid relative path:" + targetRelativePath);
        }

        // if file already exists, throw an exception
        destFile = new File(this.workingDir, modifiedPath);
        if (destFile.exists()) {
            throw new IllegalArgumentException("File already exists:" + targetRelativePath);
        }

        // standardize relative path
        String standardRelativePath = filePath.substring(workingPath.length());
        if (standardRelativePath.startsWith("/") || standardRelativePath.startsWith("\\")) {
            standardRelativePath = standardRelativePath.substring(1);
        }
        standardRelativePath = standardRelativePath.replace('\\', '/');

        // save a copy to file system
        destFile = new File(this.workingDir, modifiedPath);
        FileUtils.copyFile(sourceFile, destFile);

        // also upload to database
        FileObject file = new FileObject();
        file.setCreatedDate(new Date());
        file.setLastModified(destFile.lastModified());
        file.setPath(standardRelativePath);
        file.setContent(FileUtils.readFileToByteArray(sourceFile));
        this.fileObjectDao.persist(file);

        return destFile;
    }

    @Override
    public File find(String relativePath) throws Exception {
        File result;

        // validate input to make sure the file is under workingDir to avoid security issues
        String modifiedPath = relativePath;
        while (modifiedPath.startsWith("/")) {
            modifiedPath = modifiedPath.substring(1);
        }
        File destFile = new File(this.workingDir, modifiedPath);
        String workingPath = this.workingDir.getCanonicalPath();
        String filePath = destFile.getCanonicalPath();
        while (destFile != null && !destFile.equals(this.workingDir)) {
            destFile = destFile.getParentFile();
        }
        if (destFile == null || !filePath.startsWith(workingPath)) {
            throw new IllegalArgumentException("Invalid relative path:" + relativePath);
        }

        // do find file
        destFile = new File(this.workingDir, modifiedPath);
        if (destFile.exists()) {
            result = destFile;
        } else {
            //If the file is not existing, show photo not available.
            result = new File(this.workingDir, "/assets/images/photo_not_available.jpg");
        }

        return result;
    }

    @Override
    public synchronized void download() throws Exception {

        if (!this.workingDir.exists()) {
            Validate.isTrue(this.workingDir.mkdir());
        }

        for (FileObject fileObject : this.fileObjectDao.findAll()) {
            File file = new File(this.workingDir, fileObject.getPath());

            // remove if obsolete
            if (file.exists() && file.lastModified() != fileObject.getLastModified()) {
                FileUtils.deleteQuietly(file);
            }

            // if file not exists, save from db
            if (!file.exists()) {
                // create file
                FileUtils.writeByteArrayToFile(file, fileObject.getContent());
                Validate.isTrue(file.setLastModified(fileObject.getLastModified()));
            }
        }
    }
}
