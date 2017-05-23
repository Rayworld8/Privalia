package com.simbiotic.privalia.common.api;

import java.util.HashMap;


public class ParamsMap extends HashMap {

    @Override
    public Object put(Object key, Object value) {
        if (value == null)
            value = "";
        return super.put(key, value);
    }
}
