package com.datacenter.entity;

public interface Visitable<T> {
    void accept(Visitor<T> visitor);
}
