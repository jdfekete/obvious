# Introduction #

Each Obvious implementation has its own set of unit tests. These tests are mainly derivated from abstract unit test described in "obvious" project. All units tests are based on JUnit 4.7.


# Details #

Each Obvious implementation is based on Maven. So, tests must be found directly under the folder : src/test/java. Resources for test are found under src/test/resources.

There are two ways to run unit tests in an Obvious implementation :

  * Directly from Eclipse, by the classic way to run a JUnit test on this IDE.
  * The other is based on Maven, with the Maven command test. All the tests for the current         implementation will be runned.