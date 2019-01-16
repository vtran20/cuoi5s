package com.easysoft.ecommerce.controller.exception;

/**
 * Created by vutran on 1/14/2019.
 */
public class NailsException extends Exception{
    public NailsException () {}
    public NailsException (String exception) {super(exception);}
    public NailsException (Throwable cause) {super(cause);}
    public NailsException (String exception, Throwable cause) {super(exception, cause);}
}
