package com.easysoft.ecommerce.service.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.jdesktop.swingx.graphics.GraphicsUtilities;

public class ImageCropCommand implements FileCommand, Serializable {
    private static final long serialVersionUID = 1L;
    private File in;
    private File out;
    private int width;
    private int height;
    private int x;
    private int y;

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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
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

        BufferedImage img = ImageIO.read(in);
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();

        // validate
        if (x < 0 || y < 0 || x >= originalWidth || y >= originalHeight || width <= 0 || height <= 0)
            throw new IllegalArgumentException("Invalid width/height/offset.");

        // adjust
        int newWidth = this.width;
        int newHeight = this.height;
        if (x + newWidth > originalWidth) {
            // reduce newWidth
            newWidth = originalWidth - x;
        }
        if (y + newHeight > originalHeight) {
            // reduce newWidth
            newHeight = originalHeight - y;
        }

        // process
        try {
            // Use ImageMagick first
            cropImageUsingImageMagick(this.in, this.x, this.y, newWidth, newHeight, this.out);
        } catch (Exception e) {
            // ImageMagick doesn't work, fallback to Java2D mode
            cropImageUsingImageIO(this.in, this.x, this.y, newWidth, newHeight, this.out);
        }
    }

    private static void cropImageUsingImageMagick(File originalImage, int x, int y, int newWidth, int newHeight, File croppedImage)
            throws Exception {
        ProcessStarter.setGlobalSearchPath(System.getenv("PATH"));
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();
        op.addImage(originalImage.getAbsolutePath());
        op.crop(newWidth, newHeight, x, y);
        op.addImage(croppedImage.getAbsolutePath());
        cmd.run(op);
    }


    private static void cropImageUsingImageIO(File originalImage, int x, int y, int newWidth, int newHeight, File croppedImage)
        throws Exception {

        // get original image format
        String formatName;
        ImageInputStream iis = ImageIO.createImageInputStream(originalImage);
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            ImageReader reader = readers.next();
            formatName = reader.getFormatName();
        } finally {
            iis.close();
        }
        // exit if cannot determine format
        if (formatName == null ) return;


        // crop image
        BufferedImage img = ImageIO.read(originalImage);
        BufferedImage croppedImg = GraphicsUtilities.createCompatibleImage(img, newWidth, newHeight);
        Graphics2D g2 = croppedImg.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawImage(img,
                    0, 0, newWidth, newHeight,
                    x, y, x + newWidth, y + newHeight,
                    null);
        } finally {
            g2.dispose();
        }

        // save using original image format
        ImageIO.write(croppedImg, formatName, croppedImage);
    }
}
