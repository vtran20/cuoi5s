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

public class ImageResizeCommand implements FileCommand, Serializable {
    private static final long serialVersionUID = 1L;
    private File in;
    private File out;
    private Integer width;
    private Integer height;
    private boolean keepRatio;

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

    public void setKeepRatio(boolean keepRatio) {
        this.keepRatio = keepRatio;
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

        // fix null values for newWidth and newHeight and validate
        BufferedImage img = ImageIO.read(in);
        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();
        Integer newWidth = this.width;
        Integer newHeight = this.height;
        if (newWidth == null && newHeight == null) {
            newWidth = originalWidth;
            newHeight = originalHeight;
        } else if (keepRatio && newWidth == null) {
            newWidth = originalWidth * newHeight / originalHeight;
        } else if (keepRatio && newHeight == null) {
            newHeight = originalHeight * newWidth / originalWidth;
        } else if (keepRatio) {
            // keep ratio and specify both width and height => they are max width and max height, they need adjustments
            if (newWidth > originalWidth * newHeight / originalHeight) {
                // reduce newWidth
                newWidth = originalWidth * newHeight / originalHeight;
            } else if (newWidth < originalWidth * newHeight / originalHeight) {
                // reduce newHeight
                newHeight = originalHeight * newWidth / originalWidth;
            }
        } else if (newWidth == null || newHeight == null) {
            throw new IllegalArgumentException("You must specify both width and height when keepRatio=false.");
        }
        if (newWidth <= 0 || newHeight <= 0) throw new IllegalArgumentException("Invalid width or height.");

        // process
        try {
            // Use ImageMagick first
            resizeImageUsingImageMagick(this.in, newWidth, newHeight, this.keepRatio, this.out);
        } catch (Exception e) {
            // ImageMagick doesn't work, fallback to Java2D mode
            resizeImageUsingImageIO(this.in, newWidth, newHeight, this.keepRatio, this.out);
        }
    }

    private static void resizeImageUsingImageMagick(File originalImage, Integer newWidth, Integer newHeight, boolean keepRatio, File resizedImage)
            throws Exception {
        ProcessStarter.setGlobalSearchPath(System.getenv("PATH"));
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();
        op.addImage(originalImage.getAbsolutePath());
        if (keepRatio) {
            op.resize(newWidth, newHeight);
        } else {
            op.resize(newWidth, newHeight, "!");
        }
        op.addImage(resizedImage.getAbsolutePath());
        cmd.run(op);
    }


    private static void resizeImageUsingImageIO(File originalImage, Integer newWidth, Integer newHeight, boolean keepRatio, File resizedImage)
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


        // scale image
        BufferedImage scaledImg;
        BufferedImage img = ImageIO.read(originalImage);
        if (img.getWidth() >= newWidth || img.getHeight() >= newHeight) {
            scaledImg = GraphicsUtilities.createThumbnail(img, newWidth, newHeight);
        } else {
            scaledImg = GraphicsUtilities.createCompatibleImage(img, newWidth, newHeight);
            Graphics2D g2 = scaledImg.createGraphics();
            try {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.drawImage(img, 0, 0, newWidth, newHeight, null);
            } finally {
                g2.dispose();
            }
        }

        // save using original image format
        ImageIO.write(scaledImg, formatName, resizedImage);
    }
}
