package com.easysoft.ecommerce.web.servlet;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.captchastore.FastHashMapCaptchaStore;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CaptchaServlet extends HttpServlet {

    private static ImageCaptchaService instance = null;

    public void init(ServletConfig servletConfig) throws ServletException {

        super.init(servletConfig);
        instance = new DefaultManageableImageCaptchaService(new FastHashMapCaptchaStore(), new ListImageCaptchaEngine() {
            @Override
            protected void buildInitialFactories() {
                WordGenerator wgen = new RandomWordGenerator("ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789");
                RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(new int[]{0, 100}, new int[]{0, 100}, new int[]{0, 100});
                TextPaster textPaster = new RandomTextPaster(6, 6, cgen, true);

                BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(200, 50);

                Font[] fontsList = new Font[]{new Font("Arial", 0, 10), new Font("Tahoma", 0, 10), new Font("Verdana", 0, 10),};

                FontGenerator fontGenerator = new RandomFontGenerator(20, 35, fontsList);

                WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
                this.addFactory(new GimpyFactory(wgen, wordToImage));
            }
        }, 180, 100000, 75000);

    }


    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        byte[] captchaChallengeAsJpeg = null;
        // the output stream to render the captcha image as jpeg into
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // get the session id that will identify the generated captcha.
            //the same id must be used to validate the response, the session id is a good candidate!
            String captchaId = httpServletRequest.getSession().getId();
            // call the ImageCaptchaService getChallenge method
            BufferedImage challenge = instance.getImageChallengeForID(captchaId, httpServletRequest.getLocale());

            // a jpeg encoder
            JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
            jpegEncoder.encode(challenge);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (CaptchaServiceException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

        // flush it in the response
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    public static boolean validateCaptcha(HttpServletRequest httpServletRequest) {
        Boolean isResponseCorrect = Boolean.FALSE;
        //remenber that we need an id to validate!
        String captchaId = httpServletRequest.getSession().getId();
        //retrieve the response
        String response = httpServletRequest.getParameter("captcha");
        // Call the Service method
        try {
            isResponseCorrect = instance.validateResponseForID(captchaId, response);
        } catch (CaptchaServiceException e) {
            //should not happen, may be thrown if the id is not valid
        }

        return isResponseCorrect;
    }
}
