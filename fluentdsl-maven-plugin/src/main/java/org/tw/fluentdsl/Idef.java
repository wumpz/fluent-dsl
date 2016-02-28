package org.tw.fluentdsl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface definition.
 *
 * @author toben
 */
class Idef {

    private final String name;
    private final List<Mdef> methods = new ArrayList<>();
    private boolean hidden;

    public Idef(String name) {
        this(false, name);
    }

    public Idef(boolean hidden, String name) {
        this.hidden = hidden;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Mdef> getMethods() {
        return methods;
    }

    private void addReturnTypeToAllVoidMethods(Set<Idef> processed, Idef idef, boolean recursive) throws FluentDslException {
        if (processed.contains(this)) {
            return;
        }
        processed.add(this);

        for (Mdef mdef : methods) {
            if (mdef.getReturnType() == null) {
                mdef.setReturnType(idef);
            } else if (recursive) {
                mdef.getReturnType().addReturnTypeToAllVoidMethods(processed, idef, recursive);
            }
        }
    }

    public void addReturnTypeToAllVoidMethods(Idef idef, boolean recursive) throws FluentDslException {
        addReturnTypeToAllVoidMethods(new HashSet<Idef>(), idef, recursive);
    }

    public void addReturnTypeToAllVoidMethods(Idef idef) throws FluentDslException {
        addReturnTypeToAllVoidMethods(idef, false);
    }

    public boolean allReturnTypesSet() {
        for (Mdef mdef : methods) {
            if (mdef.getReturnType() == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return "Idef{" + "name=" + name + '}';
    }
}
