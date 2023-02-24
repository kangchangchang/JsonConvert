package com.datacenter.entity;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonConvertTreeBO<T> implements Visitable<T> {
    private List<JsonConvertTreeBO> children = new ArrayList();
    private T data;

    public JsonConvertTreeBO(T data) {
        this.data = data;
    }

    public JsonConvertTreeBO() {
    }

    public void accept(Visitor<T> visitor) {
        visitor.visitData(this, this.data);
        Iterator var2 = this.children.iterator();

        while(var2.hasNext()) {
            JsonConvertTreeBO child = (JsonConvertTreeBO)var2.next();
            Visitor<T> childVisitor = visitor.visitTree(child);
            child.accept(childVisitor);
        }

    }

    public JsonConvertTreeBO child(T data) {
        Iterator var2 = this.children.iterator();

        JsonConvertTreeBO child;
        do {
            if (!var2.hasNext()) {
                return this.child(new JsonConvertTreeBO(data));
            }

            child = (JsonConvertTreeBO)var2.next();
        } while(!child.data.equals(data));

        return child;
    }

    public JsonConvertTreeBO child(JsonConvertTreeBO<T> child) {
        this.children.add(child);
        return child;
    }

    public List<JsonConvertTreeBO> getChildren() {
        return this.children;
    }

    public void setChildren(List<JsonConvertTreeBO> children) {
        this.children = children;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
