# Introduction #

Each Obvious implementation is a Maven Project. So, in a maven environment, maven will resolve with the pom.xml file all the dependancies and build the correct java classpath.

If you want to inspect, enhance or update the code of an existing Obvious bindings, you will need to checkout it and several others modules from the google-code SVN repository. Details are given above.

# Details #

## Developing, updating and enhancing existing Obvious bindings ##

For a minimal development environment you need to the following setup. An environment using Eclipse with the Maven and Subversion plugins is assumed, but any setup supporting Maven and Subversion should do. A minimal setup consists of Obvious, ObviousX and optionally one of the bindings such as Obvious-Prefuse.

  1. Checkout the Obvious interfaces from http://obvious.googlecode.com/svn/trunk/obvious.
  1. Checkout the Obvious utils from http://obvious.googlecode.com/svn/trunk/obviousx
  1. Checkout an Obvious component e.g. http://obvious.googlecode.com/svn/trunk/obvious-prefuse

Each of these can be checked out as a separate project in Eclipse. The Maven build framework will retrieve all remaining dependencies.

## Using an Obvious binding for your project ##

In order to facilitate deployment of existing bindings, a dedicated maven repository has been set up.

The following guidelines assume your project is a maven one. To add an Obvious binding to it, you will have to complete its pom.xml file with details about our custom maven repository and the targeted Obvious-binding dependancies.

Thus, just adds the following section to describe the Obvious maven repository:

```
  <repositories>
  	<repository>
            <id>googlecode</id>
  	    <name>Obvious Repository for Maven</name>
  	    <url>http://obvious.googlecode.com/svn/repo/</url>
  	</repository>
  </repositories>
```

Then, describes the used Obvious-binding in the `<dependancies>` section. For example, if you choose the Obvious-prefuse binding, just adds the following lines:

```
    <dependency>
        <groupId>obvious-prefuse</groupId>
        <artifactId>obvious-prefuse</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

The version number can change, you can directly check for the correct one directly in the maven repository. For instance, for the prefuse binding, it can be retrieved here: http://obvious.googlecode.com/svn/repo/obvious-prefuse/obvious-prefuse/

Once this is done, the Maven build framework will retrieve all remaining dependencies: you can start using Obvious in your project.