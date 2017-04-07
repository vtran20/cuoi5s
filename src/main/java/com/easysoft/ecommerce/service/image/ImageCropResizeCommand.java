package com.easysoft.ecommerce.service.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

import javax.imageio.ImageIO;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ImageCropResizeCommand implements FileCommand, Serializable {
    private static final long serialVersionUID = 1L;
    private File in;
    private File out;
    private Integer width;
    private Integer height;

    @Override
    public void setIn(File in) {
        this.in = in;
    }

    @Override
    public void setOut(File out) {
        this.out = out;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
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
        if (width == null || height == null || width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid width or height.");

        BufferedImage img = ImageIO.read(this.in);
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();


        // resize to (width, height), keep ratio but when call resize command, use keepRatio=false to avoid missing pixels
        int newWidth = this.width;
        int newHeight = this.height;
        if (newWidth > originalWidth * newHeight / originalHeight) {
            // increase newHeight
            newHeight = originalHeight * newWidth / originalWidth;
        } else if (newWidth < originalWidth * newHeight / originalHeight) {
            // increase newWidth
            newWidth = originalWidth * newHeight / originalHeight;
        }
        ImageResizeCommand resize = new ImageResizeCommand();
        resize.setIn(this.in);
        resize.setOut(this.out);
        resize.setWidth(newWidth);
        resize.setHeight(newHeight);
        resize.setKeepRatio(false);
        resize.execute();

        // crop redundant parts
        if (newHeight > this.height) {
            // crop height
            ImageCropCommand crop = new ImageCropCommand();
            crop.setIn(this.out);
            crop.setOut(this.out);
            crop.setX(0);
            crop.setY((newHeight - this.height) / 2);
            crop.setWidth(this.width);
            crop.setHeight(this.height);
            crop.execute();
        } else if (newWidth > this.width) {
            // crop width
            ImageCropCommand crop = new ImageCropCommand();
            crop.setIn(this.out);
            crop.setOut(this.out);
            crop.setX((newWidth - this.width) / 2);
            crop.setY(0);
            crop.setWidth(this.width);
            crop.setHeight(this.height);
            crop.execute();
        }
    }
}
