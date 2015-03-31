# Introduction #

This page is a tutorial showing how to create an InfoVis application with this toolkit. It focus on the main steps: creating the data structure, the visualisation (visual abstraction) and the view.


# Creating the Obvious data structure #

There are several ways to create data structures in Obvious: by wrapping existing data structures from an Obvious compatible toolkit (i.e. Prefuse, Improvise, InfoVis toolkit, weka and Jung), by creating an empty structure and adding manually tuples to this structure or by reading a file containing data.

For this tutorial, we will use the last approach (and give hints to manually build a data structure): we want to load a CSV file containing tabular data. This file could be downloaded [here](http://code.google.com/p/obvious/source/browse/trunk/obvious-example/src/main/resources/articlecombinedexample.csv).

Since Obvious is toolkit based on several implementations, before creating data structures, the user must decide which binding to use. To specify it properly, we recommend to use the well-known  Factory pattern.

For instance, here, the following code indicates that the Obvious Prefuse binding is used for data structures:

`System.setProperty("obvious.DataFactory", "obvious.prefuse.data.PrefuseDataFactory");`

Once this is defined, the user can call the appropriate reader. Here since this example is based on a CSV file, the generic Obvious CSV importer for Table structure is called:

`CSVTableImport csv = new CSVTableImport(new File("articlecombinedexample.csv"), ',');`

Then, the table can be loaded:
`Table table = csv.loadTable();`

As indicated above, it is also possible to build manually an Obvious data structure. Three steps have to be followed: specifying a schema, building the structure from the defined schema, and adding tuple to the data structure. See the following lines of code:

```
// First the schema
Schema schema = new PrefuseObviousSchema();
schema.addColumn("columnName", String.class, "defaultValue");

// Then the table...
Table table = new PrefuseObviousTable(schema);
OR
Table table = factory.createTable(schema);

// Add tuple
int rowId = table.addRow()
table.setValue("value", rowId, columnId);

// Or if tuples already exist
table.addRow(row);
```

Most of the time, Obvious reader will handle those tasks.

# Creating the Visualization #

To create an Obvious visualization, it is also possible to use a factory or direct instantiation. We will see how to use the direct instantiation below.

To create a Visualization, the user has simply to find the appropriate visualization technique in the Obvious binding. Here, we want use a scatter plot visualization based on Ivtk:

`Visualization vis = new IvtkScatterPlotVis(table , null , "plot", param);`

The extra parameter "param" is a map of parameter for the configurable parameters of the visualization technique. Here, this map has been used to set the X and Y axis of the visualization:

```
    Map<String, Object> param = new HashMap<String, Object>();
    param.put(IvtkScatterPlotVis.X_AXIS, "id"); // xfield
    param.put(IvtkScatterPlotVis.Y_AXIS, "age"); // yfield
```

# Creating the View #

To create the Obvious View, the user has to used the View component introduced in the binding, he/she used for the visualization part.

`    JView view = new IvtkScatterPlotView(vis, null);`

Once the component is ready, apply the standard window java creation:

```
    JFrame frame = new JFrame("Scatter-plot visualization (article example)");
    JScrollPane panel = new JScrollPane(view.getViewJComponent());
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
```