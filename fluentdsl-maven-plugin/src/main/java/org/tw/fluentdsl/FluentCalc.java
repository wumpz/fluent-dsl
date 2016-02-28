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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Computes fluent sets. This is a quick implementation of this very simple
 * fluent interface compilation. Just a proove of concept. One shlould code with
 * a little more theory to get a complete language. I think the grammar so far
 * does work well for not so complicated stuff. More complex generation is done
 * in a future release.
 *
 * The grammar prevents some brackets.
 *
 * The first option would be to set the package the interfaces are generated in.
 * This calculator does generate Java interfaces.
 *
 * @author toben
 */
public class FluentCalc {

    private final Map<String, Idef> ifaces = new HashMap<>();
    private final Map<String, String> options = new HashMap<>();
    private final Deque<Idef> stack = new ArrayDeque<>();
    private static final Logger LOG = Logger.getLogger(FluentCalc.class.getName());

    public FluentCalc(String grammar) throws IOException, FluentDslException {
        this(new ByteArrayInputStream(grammar.getBytes("UTF-8")));
    }

    public FluentCalc(InputStream grammarIs) throws IOException, FluentDslException {
        FlibParser parser = ParseUtils.parser(grammarIs);
        parser.setErrorHandler(new FluentDslErrorStrategy());
        parser.removeErrorListeners();
        parser.addErrorListener(FluentDslThrowingErrorListener.INSTANCE);
        FlibParser.StartContext start = parser.start();
        start.accept(new FlibBaseVisitor() {
            @Override
            public Object visitOption(FlibParser.OptionContext ctx) {
                if (ctx.value.getText().startsWith("'")) {
                    options.put(ctx.name.getText(), ctx.value.getText().substring(1, ctx.value.getText().length() - 1));
                } else {
                    options.put(ctx.name.getText(), ctx.value.getText());
                }
                return null;
            }
        });
        start.accept(new FlibBaseVisitor() {
            @Override
            public Object visitInterfaceRule(FlibParser.InterfaceRuleContext ctx) {
                Idef idef = ifaces.get(ctx.ID().getText());
                if (idef == null) {
                    idef = new Idef(ctx.hidden, ctx.ID().getText());
                    ifaces.put(ctx.ID().getText(), idef);
                }
                LOG.log(Level.INFO, "starting interface {0}", idef.toString());
                stack.push(idef);
                super.visitInterfaceRule(ctx);
                stack.pop();
                LOG.log(Level.INFO, "stopping interface {0}", idef.toString());

                return null;
            }

            /**
             * The first interface is used to build a container for all these
             * following methods.
             *
             * @param ctx
             * @return
             */
            @Override
            public Object visitList(FlibParser.ListContext ctx) {
                LOG.info("  starting list");
                String nextInterfaceName = stack.peek().getName();
                Idef lastI = stack.peek();
                for (int i = 0; i < ctx.getChildCount(); i++) {
                    if (ctx.getChild(i) instanceof TerminalNode) {
                        nextInterfaceName = ctx.getChild(i).getText();
                        nextInterfaceName = nextInterfaceName.substring(1, nextInterfaceName.length() - 1);
                    } else {
                        //the only case all return types are set, are within oneormore 
                        //expressions. here the following entry have to be added to the
                        //last interface.
                        if (!stack.peek().allReturnTypesSet() || stack.peek().getMethods().isEmpty()) {
                            Idef idef = new Idef(getUniqueName(nextInterfaceName));

                            if (i != 0) {
                                if (ifaces.containsKey(idef.getName())) {
                                    LOG.warning("duplicate interface definition");
                                } else {
                                    ifaces.put(idef.getName(), idef);
                                }

                                try {
                                    stack.peek().addReturnTypeToAllVoidMethods(idef);
                                } catch (FluentDslException ex) {
                                    Logger.getLogger(FluentCalc.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            stack.push(idef);
                        }
                        ctx.getChild(i).accept(this);
                    }
                }
                Idef idefAbstract = null;
                while (true) {
                    idefAbstract = stack.pop();
                    if (stack.peek().equals(lastI)) {
                        break;
                    } else if (stack.isEmpty()) {
                        LOG.warning("stack broken");
                    }
                }
                LOG.info("  stopping list");
                //merge abstract interface within current top interface
                stack.peek().getMethods().addAll(idefAbstract.getMethods());
                return null;
            }

            @Override
            public Object visitWord(FlibParser.WordContext ctx
            ) {
                Mdef mdef = new Mdef(ctx.ID().getText());
                if (ctx.methodSignature() != null) {
                    if (ctx.methodSignature().methodParameters() != null) {
                        for (FlibParser.MethodParamContext param : ctx.methodSignature().methodParameters().methodParam()) {
                            mdef.addParam(param.name.getText(), param.typeName.getText());
                        }
                    }
                    if (ctx.methodSignature().returnType != null) {
                        mdef.setSimpleReturnType(ctx.methodSignature().returnType.getText());
                    }
                }
                LOG.log(Level.INFO, "     adding method {0}", mdef.toString());
                stack.peek().getMethods().add(mdef);
                return super.visitWord(ctx);
            }

            @Override
            public Object visitInterface(FlibParser.InterfaceContext ctx
            ) {
                Mdef mdef = new Mdef(true, ctx.ID().getText());
                LOG.log(Level.INFO, "     adding multi method {0}", mdef.toString());
                stack.peek().getMethods().add(mdef);
                return super.visitInterface(ctx);
            }

            @Override
            public Object visitOneOrMore(FlibParser.OneOrMoreContext ctx) {
                ctx.expr.accept(this);

                Idef idef = null;
                try {
                    idef = deepCopyInterface(stack.peek().getName() + "OneOrMore", stack.peek(), null);
                } catch (FluentDslException ex) {
                    Logger.getLogger(FluentCalc.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    stack.peek().addReturnTypeToAllVoidMethods(idef, true);
                } catch (FluentDslException ex) {
                    Logger.getLogger(FluentCalc.class.getName()).log(Level.SEVERE, null, ex);
                }
                stack.push(idef);
                return null;
            }
        }
        );

        expandInterfaceDefinitions();
        correctMethodNames();
    }

    public static final Pattern NAME_PATTERN = Pattern.compile("^(.*?)\\d*$");

    private int counter = 0;
    
    private String getUniqueName(String inputIdefName) {
        Matcher m = NAME_PATTERN.matcher(inputIdefName);

        String idefName;
        if (m.find()) {
            idefName = m.group(1);
        } else {
            idefName = inputIdefName;
        }
        
        if (!ifaces.containsKey(idefName))
            return idefName;
        
        counter++;
        
        while (ifaces.containsKey(idefName + counter)) {
            counter++;
        }
        return idefName + counter;
    }

    /**
     * produce a deep copy of this interfaces structure.
     *
     * @param iStartName
     * @param start
     * @param end
     * @return
     * @throws FluentDslException
     */
    private Idef deepCopyInterface(String iStartName, Idef start, Idef end) throws FluentDslException {
        Map<String, Idef> publicInterfaces = new HashMap<>();
        Map<String, Idef> createdInterfaces = new HashMap<>();
        Idef copy = deepCopyInterface(iStartName, publicInterfaces, createdInterfaces, start, end);
        ifaces.putAll(publicInterfaces);
        return copy;
    }

    private Idef deepCopyInterface(String iStartName, Map<String, Idef> publicInterfaces, Map<String, Idef> createdInterfaces, Idef start, Idef end) throws FluentDslException {
        String ifacename = iStartName != null ? iStartName : start.getName();
        if (createdInterfaces.containsKey(ifacename)) {
            return createdInterfaces.get(ifacename);
        }
        Idef copy = new Idef(getUniqueName(ifacename));
        createdInterfaces.put(start.getName(), copy);
//        if (ifaces.containsKey(start.getName())) {
        publicInterfaces.put(copy.getName(), copy);
//        }

        for (Mdef template : start.getMethods()) {
            Mdef mdef = new Mdef(template);
            if (mdef.getReturnType() != null) {
                mdef.setReturnType(deepCopyInterface(null, publicInterfaces, createdInterfaces, template.getReturnType(), end != null ? end : copy));
            } else {
                mdef.setReturnType(end != null ? end : copy);
            }
            copy.getMethods().add(mdef);
        }

        return copy;
    }

    /**
     * Get all calculated interface definitions.
     *
     * @return
     */
    Map<String, Idef> getIfaces() {
        return ifaces;
    }

    /**
     * Get all specified grammar options.
     *
     * @return
     */
    public Map<String, String> getOptions() {
        return options;
    }

    private void expandInterfaceDefinitions() throws FluentDslException {
        for (Idef idef : ifaces.values()) {
            List<Mdef> methodsToRemove = new ArrayList<>();
            List<Mdef> methodsToAdd = new ArrayList<>();
            for (Mdef mdef : idef.getMethods()) {
                if (mdef.isMultiMethod()) {
                    methodsToRemove.add(mdef);
                    Idef mm = ifaces.get(mdef.getName());
                    if (mm == null) {
                        throw new FluentDslException("unknown interface " + mdef.getName());
                    }
                    for (Mdef template : mm.getMethods()) {
                        Mdef m = new Mdef(template.getName());
                        m.setReturnType(mdef.getReturnType());
                        m.setSimpleReturnType(template.getSimpleReturnType());
                        methodsToAdd.add(m);
                    }
                }
            }
            idef.getMethods().removeAll(methodsToRemove);
            idef.getMethods().addAll(methodsToAdd);
        }
    }

    /**
     * The names are corrected to get no duplicate method names. This could be
     * improved to use parameters as well.
     *
     * @param mdef
     * @return
     */
    private String getMethodSignature(Mdef mdef, int counter) {
        return mdef.getName() + (counter == 0 ? "" : counter);
    }

    /**
     * Java does not allow methods with same parameter signature and a different
     * return type. Therefore here are method names corrected by a index value.
     */
    private void correctMethodNames() {
        if ("false".equals(options.get("correctMethodNames"))) {
            return;
        }

        Set<String> usedNames = new HashSet<>();
        for (Idef idef : ifaces.values()) {
            if (!idef.isHidden()) {
                usedNames.clear();
                for (Mdef mdef : idef.getMethods()) {
                    int c = 0;
                    while (usedNames.contains(getMethodSignature(mdef, c))) {
                        c++;
                    }
                    mdef.setName(getMethodSignature(mdef, c));
                    usedNames.add(mdef.getName());
                }
            }
        }
    }
}
