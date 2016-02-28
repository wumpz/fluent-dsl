package org.tw.fluentdsl;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Method definition.
 *
 * @author toben
 */
class Mdef {

    private String name;
    private Idef returnType;
    private boolean multiMethod = false;
    private final Map<String, String> params = new LinkedHashMap<>();
    
    private String simpleReturnType;

    /**
     * Simple return type is always overridden from an idef return type. This comes
     * into play if not interface is needed.
     *
     * @return the value of simpleReturnType
     */
    public String getSimpleReturnType() {
        return simpleReturnType;
    }

    public void setSimpleReturnType(String simpleReturnType) {
        this.simpleReturnType = simpleReturnType;
    }


    public Mdef(String name) {
        this(false, name);
    }

    public Mdef(boolean multiMethod, String name) {
        this.multiMethod = multiMethod;
        this.name = name;
    }
    
    public Mdef(Mdef template) {
        this(template.isMultiMethod(), template.getName());
        returnType = template.returnType;
        params.putAll(template.getParams());
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public Idef getReturnType() {
        return returnType;
    }

    public void setReturnType(Idef returnType) throws FluentDslException {
        this.returnType = returnType;
    }

    public boolean isMultiMethod() {
        return multiMethod;
    }

    @Override
    public String toString() {
        return (multiMethod ? "interface " : "") + name + params + " return " + returnType;
    }

    void addParam(String name, String type) {
        params.put(name, type);
    }
    
    public Map<String, String> getParams() {
        return params;
    }
}
