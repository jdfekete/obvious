# Introduction #

The toolkit contains one interface for its view part and currently one abstract class supporting views based on Swing. More abstract classes could follow based on other computer graphics library (OpenGL...).

Content :



# View #

View is mainly a tag interface, it provides few methods. This interface mainly ensures listener's support (add, remove and get).

In obvious, view implementations are intended to be specialized for a particular computer graphics library in an abstract class, like JView (see next session).

# JView #

JView is an abstract class implementing View interface. This class is dedicated to the Swing library. It simply adds an abstract method to return a JComponent corresponding to the component containing the obvious view. All the current toolktis that have a View part in their Obvious binding currently used JView to display component.

Since Obvious adopts the monolithic approach for the Visualization and then provides monolithic visualization component, views are intended to be built from them. That is why Visualizations based on a binding must be used with Views of the same binding. The following code shows how to use JView with the Prefuse Obvious binding:

```
Visualization vis = new IvtkTimeSeriesVis( table , null , ” timeseries ”, params);
// No name, predicate and extra parameter
View jView = new IvtkObviousView(vis, null, null, null);
```

If a incompatible visualization is given to a view, an exception (ObviousException) will be thrown.

# ViewListener #

ViewListener is an interface. It tags a listener implementation dedicated to an obvious view.