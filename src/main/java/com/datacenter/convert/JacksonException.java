package com.datacenter.convert;

/**
 * @author pc
 * @date Create in  2023/2/22
 */
public class JacksonException extends RuntimeException {

    private String code;
    private String msg;

    public JacksonException(String code,String msg) {
        super(code);
    }
}
