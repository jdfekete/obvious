# Introduction #

This page presents next major steps for the Obvious Project.

# Future features #

Future features mostly concerns the visualization/view (including interaction model) parts of Obvious. Since for the moment, no consensus emerges on how to abstractly describe these parts. Thus, several points concerning this issue, where consensus can be reached, have been identified:
**propose a shared implementation for graph layout computation** unify the mapping of data value to scalar value (color, coordinates...)
**unify selection management (implementation of a cross-toolkit brushing and linking)** unify visualization scale, tick-mark and tick-label management

# Portability of Obvious #

Since Obvious is mosty based on intefaces, and tend to not use Java idioms, its syntax is not Java dependant. That is why Obvious could be described as a language neutral API.

The idea is to port Obvious to others languages such as C++, C# or JavaScript where InfoVis tools exist. Obvious can become a multi-language API such as the Document Object Model API that has several bindings for major languages.

# Reach other visualizations #

For the moment, Obvious is made to unify the sofware infrastructure for InfoVis toolkits used in visual analytics. However, VA often needs to combine InfoVis with others visualizations such as Scientific and GeoSpatial ones. Obvious could be a good candidate to unify some parts of the software infrastructure for these visualiaztions.

Scientific and GeoSpatial visualizations use more complex geometrical data structure than InfoVis ones. That is why currently to build an application using different kinds of visualizations, developers have to maintain separate stages of the visualization pipelines and to coordinate them with custom mechanisms.

That is why combining these different fields of visualization at the software infrastructure would clarify patterns and allow developers to build application where the usability of mixed visualization apps would be improved.

This is a long-term goal since it requires important discussions between different communities.

# Community building #

Obvious has been built as a consensual and reference implementation through a non formal community driven process.

For the future, the Obvious community has to decide if it wants to continue with the community driven model (Java community process or Boost community process) or to switch to the consortium driven model (W3C).