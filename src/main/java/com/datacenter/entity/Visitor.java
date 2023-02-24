package com.datacenter.entity;

/**
 * @author pc
 */
public interface Visitor<T> {
    Visitor<T> visitTree(JsonConvertTreeBO<T> jsonConvertTreeBO);

    void visitData(JsonConvertTreeBO<T> parent, T data);
}
