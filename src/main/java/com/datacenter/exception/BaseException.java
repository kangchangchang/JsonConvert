package com.datacenter.exception;

/**
 * @author pc
 * @date Create in  2023/2/23
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



public class BaseException extends RuntimeException implements IBaseCodeType {
    private static final long serialVersionUID = 1L;
    protected Object[] args;
    private String code;
    private String msg;

    public BaseException(IBaseCodeType baseCodeType) {
        this(baseCodeType.getCode(), baseCodeType.getMsg());
    }

    public BaseException(String code, String msg) {
        this(new IBaseCodeType() {
            public String getCode() {
                return code;
            }

            public String getMsg() {
                return msg;
            }
        }, msg);
    }

    public BaseException(IBaseCodeType baseCodeType, String msg, Object... args) {
        super(msg);
        this.code = baseCodeType.getCode();
        this.msg = baseCodeType.getMsg();
        this.args = args;
    }

    public BaseException(IBaseCodeType baseCodeType, Throwable cause, String msg, Object... args) {
        super(msg, cause);
        this.code = baseCodeType.getCode();
        this.msg = baseCodeType.getMsg();
        this.args = args;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}

