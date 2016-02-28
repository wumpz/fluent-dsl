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

import java.util.Map;

/**
 * Write Interface sources from fluent calculation.
 *
 * @author toben
 */
public class WriteInterfaces {

    private final FluentCalc calc;

    public WriteInterfaces(FluentCalc calc) {
        this.calc = calc;

        writeInterfacesDown();
    }

    private void writeInterfacesDown() {
        for (Idef idef : calc.getIfaces().values()) {
            if (!idef.isHidden()) {
                writeInterfaceDown(buildTypeName(idef.getName()), buildInterfaceText(idef));
            }
        }
    }

    /**
     * Get options from grammar file.
     *
     * @param name
     * @return
     */
    protected final String getOption(String name) {
        return calc.getOptions().get(name);
    }

    private String buildInterfaceText(Idef idef) {
        StringBuilder b = new StringBuilder();
        if (calc.getOptions().containsKey("package")) {
            b.append("package ").append(calc.getOptions().get("package")).append(";\n\n");
        }
        b.append("public interface ").append(buildTypeName(idef.getName())).append(" {\n");

        for (Mdef mdef : idef.getMethods()) {
            b.append("    ").append(mdef.getReturnType() == null ? (mdef.getSimpleReturnType() == null ? "void" : mdef.getSimpleReturnType()) : buildTypeName(mdef.getReturnType().getName()))
                    .append(" ").append(mdef.getName()).append("(");
            for (Map.Entry<String, String> item : mdef.getParams().entrySet()) {
                b.append(item.getValue()).append(" ").append(item.getKey()).append(",");
            }

            if (!mdef.getParams().isEmpty()) {
                b.setLength(b.length() - 1);
            }
            b.append(");").append("\n");
        }

        b.append("}");
        return b.toString();
    }

    protected String buildTypeName(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    protected void writeInterfaceDown(String name, String source) {
        System.out.println(name + "=" + source);
    }
}
