package org.sorincos.newtumble.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Blog {
    private Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    // @Override
    // public String toString() {
    // return "Quote{" +
    // "type='" + type + '\'' +
    // ", value=" + value +
    // '}';
    // }
}