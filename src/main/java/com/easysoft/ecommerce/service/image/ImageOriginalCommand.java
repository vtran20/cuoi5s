package com.easysoft.ecommerce.service.image;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;

public class ImageOriginalCommand implements FileCommand, Serializable {
    private static final long serialVersionUID = 1L;
    private File in;
    private File out;

    @Override
    public void setIn(File in) {
        this.in = in;
    }

    @Override
    public void setOut(File out) {
        this.out = out;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public void execute() throws Exception {

        // validate in
        if (!this.in.exists()) throw new IllegalArgumentException("Input file does not exist:" + this.in.getAbsolutePath());

    }

}
