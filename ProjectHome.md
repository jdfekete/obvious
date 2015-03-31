# Context #

This project has been launched during the 2008 VisMaster Workshop on Visual Analytics Software Architecture, held Dec. 4-6th 2008, Paris.

Visual Analytics requires the management of huge datasets in a human‐centered process. Designing a software infrastructure for that scale is already a challenge; designing several is a loss of resources both for research and industry. The workshop will gather leading researchers and practitioners involved in designing software infrastructures for Visual Analytics systems to find where the consensus are, what can be standardized and plan a program to solve important issues when no clear consensus exists yet. There is a consensus on the architecture of information visualization systems according to several publications of the field (the reference model of Card, Mackinlay and Shneiderman, toolkits such as Prefuse, the InfoVis Toolkit and Tulip). However, Geographical visualization (with standardization conducted by the “Open Geospatial Consortium”) and statistics involve richer data types than the ones used for classical visualization, thus require additional or more complex software design patterns. An initial step consists in adopting a standard on an architecture that can manage diverse data types to foster interoperability and component sharing. This standard will not be one implementation but rather a set of interfaces described in a language neutral specification (such as IDL as adopted by the W3 Consortium for XML related application programming interfaces) and agreement by partners to adhere to these interfaces for their research systems and products. An additional benefit of this scheme is the possibility to define actual bindings for different programming languages; this is important due to the proliferation of technological platforms in the recent years, including C++, Java, C# and JavaScript/ActionScript to name a few.

While we believe the initial consensus will concern table‐oriented data and simple networks, several important issues will be left for future work. Examples include asynchronous analytical computation (performing long computations while interactively presenting the most up‐to‐date results to the analyst), history management, deep integration with databases or the management of dynamic data. The workshop will create a list of the remaining issues and propose an agenda to address them in order of importance.

# Documentation #

  * [User guide](http://code.google.com/p/obvious/wiki/UserGuide)
  * [obvious wiki page](http://code.google.com/p/obvious/wiki/WikiIndex)
  * [obvious Javadoc](http://code.google.com/p/obvious/wiki/JavadocLinks)
  * [VAST article describing Obvious](http://www.aviz.fr/~fekete/ps/obvious.pdf)

# Related Links: #

  * [Future work for Obvious](http://code.google.com/p/obvious/wiki/FutureWorks)
  * [2004 Workshop on Information Visualization Software Infrastructures](http://vw.indiana.edu/ivsi2004/)
  * [2007 Workshop on Information Visualization Software Infrastructures](https://nwb.slis.indiana.edu/events/ivsi2007/)
  * [This project is partially funded by the EEC Vismaster project](http://www.vismaster.eu/)
