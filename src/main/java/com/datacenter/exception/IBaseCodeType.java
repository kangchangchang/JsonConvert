package com.datacenter.exception;

/**
 * @author pc
 * @date Create in  2023/2/23
 */
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface IBaseCodeType {
    ConcurrentMap<String, IBaseCodeType> pool = new ConcurrentHashMap();

    String getCode();

    String getMsg();
}