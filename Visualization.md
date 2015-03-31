# Introduction #

This parts describes the Visualization abstract class and its associated classes in obvious toolkit.

Infovis toolkits often uses [polylithic](http://www.infovis-wiki.net/index.php?title=Polylithic_design) or [monolithic](http://www.infovis-wiki.net/index.php?title=Monolithic_design) design, specialy to implement the visualization part. For the moment, Obvious only propose monolithic visualization, since unlike the data model no consensus among the InfoVis community has been reached to describe visualizations as polylithic components. That is why, Obvious propose to model visualizations as a black box where existing visualization components can be wrapped.

Thus, all visualizations introduced in Obvious are monolithic. When the Obvious binding is based on a monolithic toolkit, to create visualizations it, developers simply need to wrap existing visualization in the black.

However when the binding is based on a polylithic toolkit like Prefuse, it is still possible to use the Obvious black box. A developer can always describe and encapsulates his polylithic component in a monolithic one and then can wrap it into the black box.


# Visualization (abstract class) #

## How to create an Obvious visualization? ##

Unlike the data model, Visualization is not an interface, but is implemented as an abstract class. To build a new obvious visualization instance, it is needed to provide an obvious data structure instance (among Table, Network and Tree<Node, Edge>), a [predicate](Predicate.md) (can be null), a visualization technic name (can be null) and a map of parameters (can be null).

## Optional parameters ##

As standard monolithic visualization components, the Obvious black box visualization admits several extra parameters.

Optional parameters for obvious visualizations are contained in a Java map. Known parameters are identified by a key. This key is a java String (list of known keys is available below). If a visualization is provided with an unknown parameter, it will be simply ignored.

The following table presents all existing parameters for visualization :

| **Parameter key** | **Value Type** | **Implementation** | **Description** |
|:------------------|:---------------|:-------------------|:----------------|
| X\_AXIS | String | obvious-ivtk / obvious-prefuse (scatterplot) | Column used for x values |
| Y\_AXIS | String | obvious-ivtk /obvious-prefuse  (scatterplot) | Column used for Y values |
| LAYOUT | String | obvious-jung |Classpath for JUNG layout used for the current visualization |
| GROUP\_NAME | String | obvious-prefuse | Group Name used for the underlying prefuse visualization |
| DIRECTED | Boolean | obvious-prefuse | Indicates if the obvious graph is directed |
| NODE\_KEY | String | obvious-prefuse | Column used for nodes ID |
| LABEL\_KEY | String | obvious-prefuse (could be used by other impl.)| Column used for nodes label |
| SHAPE | String | obvious-prefuse (could be used by other impl.)| N/A |

# Creating visualization #

## VisualizationFactory (abstract class) ##

VisualizationFactory is also an abstract class. It provides a list of all visualization technics available for an implementation.

To create a visualization, it is needed to provide an obvious data structure, a [predicate](Predicate.md) (can be null) and a visualization technic name and a map of parameters.

```
Map params = new HashMap();
params.put("x", "id");
params.put("y", "age");
Visualization vis = VisualizationFactory.getInstance ()
  .createVisualization(table , null , "scatterplot", params);
```

## Direct instantiation ##

It is still possible to directly instantiate visualization components with the appropriate Java constructor. For example, if the developer wants to create a time series based on the InfoVis Toolkit, the following lines are needed:

```
new IvtkTimeSeriesVis( table , null , ” timeseries ”, params)
```