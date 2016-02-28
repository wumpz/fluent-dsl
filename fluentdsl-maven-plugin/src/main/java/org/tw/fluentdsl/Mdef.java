/*
 * #%L
 * fluentdsl-maven-plugin
 * %%
 * Copyright (C) 2016 fluentdsl
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
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
