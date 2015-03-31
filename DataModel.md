# Introduction #

The toolkit contains interface to describe tables, graphs, trees, tuples, nodes, and edges. Generally (and mandatory...), in obvious, a schema is used to create and describe these structures.

This section presents each interface and gives the class diagram of the obvious data model.

<b> Content </b>



# Interfaces #

## Schema ##

The Schema interface describes the structure of obvious tuples (i.e. table rows, nodes and edges). It contains columns description and methods to add/remove columns. As there is no column class in obvious (unlike other toolkits as ivtk), using this interface is the only way in obvious to get information about a column from an existing tuple.

To add a new column to a schema, you simply needs its name (java String), its type (java Class<?>) and its default value. In the following example, the developer adds a "name" column and an "age" column to an existing schema :

```
schema.addColumn("name", String.class, "John Doe");
schema.addColumn("age", Integer.class, new Integer(25));
```

To retrieve information about a specific column from a schema, you can use the column index or the column name.

## Tuple, nodes and edges ##

### Tuple ###

Most data structures in obvious are composed of tuples. For tables, a tuple corresponds to a table row, and in networks and trees a tuple corresponds to a node or an edge.

A tuple is composed of several columns. They are described by a Schema instance. Thus, each cell of a tuple can be accessed or set by calling get / set method with column's name or index as parameter.

```
// We assume the tuple used the schema introduced in the Schema section.
String tupleNameValue = (String) tuple.get(0);
tupleNameValue.equals(tuple.get("name")); // returns true
```

In this example, the developer used the tuple's get method -returning an object- and then casts the result to String. With obvious tuple, you can directly get the result as a String (or another java's primary type) by using the getString method. In addition, it is possible to check the type of the get method. This is shown by the following example :

```
String tupleNameValue = "";
// Type checking
if (tuple.canGetString(0)) {
   tupleNameValue = tuple.getString(0);
} else {
   throw new ObviousRuntimeException("Failed...");
}
```

### Node ###

Node interface is strictly identical to Tuple interface. This interface is a tag used to distinguish node tuples from edge tuples in obvious graph.

### Edge ###

Edge interface is strictly identical to Tuple interface. This interface is a tag used to distinguish edge tuples from node tuples in obvious graph.

## Data (Tag) ##

This interface is empty. It is mainly a tag that obvious's data structure implements (such as Table, Graph, Tree...).

## Table ##

A Table in Obvious is based on a schema and composed of Tuple (row). So, a table is a set of tuple sharing the same schema.

The Table interface provides methods to get the surrounding schema, adding / removing row,
getting/setting row's cell values,iterating on the row and listening to table changes. The developer can access to a row with the row index.

## Graph, Network, Tree ##

### Graph ###

Graph<V, E> is a generic interface :

  * V corresponds to the java class or interface used to modelize vertices in a specific implementation of Graph.
  * E corresponds to the java class or interface used to modelize node in a specific implementation of Graph.

Graph interface provides method to add/remove vertices and edges, to iterate on vertices and edges and some functions to get neighborhood vertices, entering / exiting edges for a vertices...

Oriented and non-oriented graph could be represented with this interface. Theoretically,it supports even graphs having non oriented and oriented edges. Each time, the developer adds an edge, he has to precise if it is oriented or not. A java Enum is included in the interface, it allows the description edge's orientation. See the following example :

```
// Let's suppose there are three existing nodes (node1, node2 and node3 V instances), two existing edges (edge1 and edge2 E instances) and an existing graph (Graph<V,E> instance).
graph.addNode(node1);
graph.addNode(node2);
graph.addNode(node3);
graph.addEdge(edge, node1, node2, Graph.DIRECTED); // adding a directed edge between n1 n2
graph.addEdge(edge, node1, node3, Graph.UNDIRECTED); // non directed edge between n1 n3
```

For the moment, all existing obvious implementations uses a specific implementation of Graph Interface, Network, described in the following section.

### Network ###

Network interface is a particular implementation of Graph interface : Graph<Node, Edge>. It provides all method from Graph<V,E> interface and adds several new functionnalities such as NetworkListener support (similar to TableListener) and the possibility to get the underlying nodes and edges tables.

Currently, obvious data model implementation such as obvious-ivtk, obvious-prefuse and obvious-jung used Network to implement Graph interface.

### Forest and Tree ###

## Forest ##

Forest<V, E> interface in obvious is used to modelize the union of several disconnected tree. It simply extends the Graph<V, E> and adds a new method to get the java collection of Tree instances contained in the Forest instance.

## Tree ##

Tree<V,E> interface in obvious extends the Forest<V, E> interface. This interface provides useful methods for Tree to get its depth, its height, its roots and parent/child nodes/edges for a specific node.

Currently, all the existing implementation of Tree interface extends Graph<Node, Edge> and are thus derived from Network.

# Different ways to instantiate and fill data structures #

Obvious introduces different mechanisms to create and fill data structures. This part presents all the possibilites offered by Obvious to complete those tasks.

## Creating Obvious data structures ##

To instantiate data structures in Obvious, it is possible to use the DataFactory class or to use direct instantiation.

The direct instantiation supposes the developper directly calls one of the constructors defined in a Table, Network or Tree implementations of Obvious. Most of the time, they only take one or two schemas as arguments. The following lines of code gives examples for Prefuse, InfoVis  toolkit and Jung:

```
// Definition of Obvious schemas
Schema tableSchema, nodeSchema, edgeSchema;
[...]
// Creating an Obvious table based on Ivtk
Table table = new IvtkObviousTable(tableSchema);
// Creating an Obvious network based on Prefuse
Network network = new PrefuseObviousNetwork(nodeSchema, edgeSchema);
// Creating an Obvious tree bases on Jung
Tree tree = new PrefuseObviousTree(nodeSchema, edgeSchema);
```

However, Obvious also proposes data factories based on the well known factory pattern to create data structures. Each Obvious binding has its own factory placed in its data subpackage.

First, the classpath of the data factory must be set up via the obvious.DataFactory property, then one instance of DataFactory must be retrieved and a creation method called (based on schema, wrapped structures from bound toolkits and optionnal extra parameters). This is illustrated by the following line of codes :

```
// Sets the factory path, here Obvious-Jung is used
String factoryPath = "obvious.jung.data.JungDataFactory";
System.setProperty("obvious.DataFactory", factoryPath);*
// Creates the instance
DataFactory dFactory = DataFactoryImpl.getInstance();
Network network = dFactory.createGraph(nodeSchema, edgeSchema);
```

## Filling Obvious data structures ##

To fill an Obvious network, it is possible to do it manually or to use external data (file, SQL source) to import with a reader.

To do it by hand, it is simply to create Tuple (or Node/Edge) instances and adds it to the structure with the corresponding method:

```
// Adds an empty row and then set up the values...
int row = table.addRow();
table.set(0, value);

// Adds a pre-existing tuple
table.addRow(tuple);
```

Obvious proposes a container for Java table to store values in a pre-existing tuple (TupleImpl) just to store and load data directly with addRow method.

The other way is to use an existing reader for a file format (CSV, GraphML...) and to directly import its content into an Obvious structure:

```
CSVTableImport csv = new CSVTableImport(newFile("example.csv"), ',');
Table table = csv.loadTable();
```

# Data model : class diagram #

![http://obvious.googlecode.com/svn/VAST2011/figures/obviousdataclass.png](http://obvious.googlecode.com/svn/VAST2011/figures/obviousdataclass.png)