All toolkits binded to Obvious follows the InfoVis reference model described by Ed Chi and updated by Card, Mackginlay and Shneiderman.

This model can be described by the following schema:

![http://obvious.googlecode.com/svn/VAST2011/figures/infovisstandard.png](http://obvious.googlecode.com/svn/VAST2011/figures/infovisstandard.png)

Information visualization applications are separated in three stages:

  * _data_: raw data, data contained in a file or in a data base are loaded into a specific in-memory database manager where data are organized into rows and/or columns
  * _visualization_: visualization is the stage where the in-memory structure is tagged with visual attributes for each (or a group) rows with simple attributes (such as color, coordinates, shapes). These attributes are used to compute the display of the visualization
  * _view_: the view is the reprensation of the visualization given to the user and where he/she can control.