# fluent-dsl

Simple proove of concept of http://blog.jooq.org/2012/01/05/the-java-fluent-api-designer-crash-course/.

## very simple Sql dsl

Here is the grammar for this very simplistic sql dsl.

~~~java
options {
    package='org.tw.fluentdsl.examples.sql';
}

#builder: build returns String;

select: column+ table+ (where | #builder) (orderby | #builder) #builder;
~~~

The maven plugin creates a set of interfaces to provide the architecture for this dsl.

## Introduction to the fdsl - file

The fluent interfaces are defined within a **fdsl** file. 

The syntax is very easy to grasp. First there is an option block were someone can tell the generator in which package the created interfaces should live:

~~~java
options {
    package='org.tw.fluentdsl.examples.sql';
}
~~~

After that the interface rules follow. You define a start interface name and provide the following method rules. fluentdsl supports here 
the expressions:
* *( expr )*
* *expr | expr*: simple or of expressions
* *expr+*: one or more of this expression
* *expr optname expr*: list of expressions with a provided subinterface name for the return value of the first expression.
* *method*: finally a method
* *#interface*: or an interface to expand here (look at the example **#builder**)

The interface rule itself is provided by this syntax:

~~~java
interfacename: method expression;
~~~

All interfaces are generated except those that start with a **#** (look at the example **#builder** interface).

Methods can be provided only with a name or a fully defined one with parameters and return value. Again look at the example: *build* method is defined with a String return value:

~~~java
testiface: mymethod(String value1, String value2) returns int;
~~~

## Maven Plugin usage

The *fdsl* - files are provided within the *src/fluentdsl* directory and the interfaces are generated within *generated-sources*.
The plugin itself is configured with something like this:

~~~xml
<plugin>
    <groupId>org.tw</groupId>
    <artifactId>fluentdsl-maven-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
    <executions>
        <execution>
            <id>default-descriptor</id>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
~~~
