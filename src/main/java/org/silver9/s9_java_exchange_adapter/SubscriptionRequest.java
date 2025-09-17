package org.silver9.s9_java_exchange_adapter;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.ArrayList;

@EntityScan
public class SubscriptionRequest {

    public String method;
    public ArrayList<String> params;
    public String id;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                "method='" + method + '\'' +
                ", params=" + params +
                ", id='" + id + '\'' +
                '}';
    }
}
