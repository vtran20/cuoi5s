package com.easysoft.ecommerce.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: FileObject
 *
 */
@Entity
@Table (name="file_object")
public class FileObject extends AbstractEntity  {

    private static final long serialVersionUID = 1L;

    private long lastModified;
    private String path;
    private byte[] content;

    public FileObject() {
        super();
    }

    @Basic(optional = false)
    @Column(unique=true, nullable=false)
    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic(optional = false)
    @Column(nullable=false)
    public long getLastModified() {
        return this.lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    @Lob
    @Basic(fetch=FetchType.LAZY)
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
