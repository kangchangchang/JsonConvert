package com.datacenter.exception;

/**
 * @author pc
 * @date Create in  2023/2/23
 */

public class ServiceException extends BaseException {
    public ServiceException(IBaseCodeType baseCodeType) {
        super(baseCodeType);
    }

    public ServiceException(String code, String msg) {
        super(code, msg);
    }

    public ServiceException(IBaseCodeType baseCodeType, Throwable throwable) {
        super(baseCodeType, throwable, (String)null, new Object[0]);
    }

    public ServiceException(IBaseCodeType baseCodeType, String msg, Object... args) {
        super(baseCodeType, msg, args);
    }

    public ServiceException(IBaseCodeType baseCodeType, Throwable cause, String msg, Object... args) {
        super(baseCodeType, cause, msg, args);
    }

    public ServiceException(BaseException exception) {
        super(exception, exception, exception.getMessage(), new Object[0]);
    }
}
