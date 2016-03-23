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

## Introduction to DSL file
