# Introduction #

Since for the moment, all Obvious bindings are written in Java, the documentation is available as Javadoc. There are several ways to extract it.

# Maven #

Each Obvious implementation is a maven project. So in a maven environment, to extract the Javadoc, just run the javadoc maven command to extract the Javadoc:

`mvn javadoc:javadoc`

If you use Eclipse and the m2Eclipse plugin, you can use the UI to extract the Javadoc. Just right click on your Eclipse Maven Project then select _Run As_ then _Maven Build..._ and then in the appearing window, indicate _javadoc:javadoc_ in the field named _goal_.

In both cases, you can find the generated documentation under the following path:
```target/site/apidocs``

# Java #

It is also possible to generate the JavaDoc in the Java way, with the commandline:

`javadoc [ options ] packagename javadoc [ options ] filenames`