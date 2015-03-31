# Introduction #

This pages describes the content of the Obvious project. First, it gives informations about each sub-project of Obvious. It clarifies also dependancies and licenses problems.

The Obvious API used the BSD license. It could be used in non-commercial and commercial projects.

# Different implementations #

Most of sub-projects implements Obvious interfaces and its sub-packages (obvious.data, obvious.viz, obvious.view and obvious.utils).

  * <b> obvious-api </b> : initial version of the obvious API. It has been written during the workshop (in Paris December 2008).
  * <b> obvious </b> : current version of the obvious API. It uses the structure given in the introduction.
  * <b> obvious-c++ </b> : a port of the Obvious API for C++. It is currently a work in progress.
  * <b> obvious-example </b> : contains several simple examples and applications using all obvious implementations.
  * <b> obvious-cytoscape </b> : implementation using <a href='http://cytoscape.org/'>Cytoscape framework </a>. Dependancies are : obvious and cytoscape framework (under GNU Library or Lesser General Public License (LGPL)).
  * <b> obvious-improvise </b> : implementation using <a href='http://www.cs.ou.edu/~weaver/improvise/'> Improvise </a> Dependancies are : obvious, improvise and oblivion librairies. For the moment, there is no maven repository containing oblivion and improvise librairies. However, it is possible to directly add oblivon and improvise jar file to your local maven repository. Both librairies are under the GPL licence.
  * <b> obvious-ivtk </b> : implementation using <a href='http://ivtk.sourceforge.net/'>infovis toolkit framework </a>. Dependancies are : obvious and infovis toolkit framework (under MIT license)
  * <b> obvious-jdbc </b> : experimental project. BSD license.
  * <b> obvious-jung </b> : implementation using <a href='http://jung.sourceforge.net/'> JUNG framework </a>. Dependancies are : obvious and Jung framework (under BSD license).
  * <b> obvious-prefuse </b> : implementation using <a href='http://prefuse.org/'> Prefuse framework </a>. Dependancies are : obvious and Prefuse framework (under BSD license).
  * <b> obviousx </b> : it is an utils package. It contains mainly reader and writer for CSV and GraphML. It also hosts wrappers allowing to use obvious data structures as ivtk and prefuse data structures.
  * <b> obviousx-rminer </b> : implementation using <a href='http://rapid-i.com/'> the RapidMiner toolkit. Dependancies are : obvious and the RapidMiner toolkit (under AGPL license).<br>
<ul><li><b> obviousx-weka </b> : implementation using <a href='http://www.cs.waikato.ac.nz/ml/weka/'> the Weka framework </a>. Dependancies are : obvious and the Weka Framework (under GNU GPL license).<br>
</li><li><b> obviousx-example </b> :it contains example for the obviousx package.<br>
</li><li><b> obviousx-jdbc </b> : experimental project. BSD license.</li></ul>

<h1>Organization of the different implementations</h1>

<h2>One toolkit four parts</h2>

The toolkit is divided in four parts :<br>
<br>
<ul><li><b>obvious.data</b> : the data model <a href='DataModel.md'>(complete article)</a>
</li><li><b>ovious.vis</b> : the visualization part of the toolkit <a href='Visualization.md'>(complete article)</a>
</li><li><b>obvious.view</b> : the view part of the toolkit <a href='View.md'>(complete article)</a>
</li><li><b>obviousx</b> : this package contains usefull classes for obvious toolkit (wrappers, readers, writters...)</li></ul>

<h2>Composition of the different bindings</h2>

<table><thead><th> <b>Binding</b> </th><th> <b>obvious.data.</b> </th><th> <b>obvious.vis</b> </th><th> <b>obvious.view</b> </th></thead><tbody>
<tr><td> <i>Infovis Tookit</i> </td><td> Yes </td><td> Yes </td><td>Yes </td></tr>
<tr><td> <i>Prefuse</i> </td><td>Yes </td><td>Yes </td><td>Yes </td></tr>
<tr><td> <i>Jung</i> </td><td> Yes </td><td> Yes </td><td> Yes </td></tr>
<tr><td> <i>Improvise</i> </td><td>Yes </td><td> No </td><td> No </td></tr>
<tr><td> <i>Cytoscape</i> </td><td> Yes </td><td> No </td><td> No </td></tr>
<tr><td> <i>RapidMiner</i> </td><td> Yes </td><td> No </td><td> No </td></tr>
<tr><td> <i>Weka</i> </td><td>Yes </td><td> No </td><td>No </td></tr>
<tr><td> JDBC </td><td> Yes </td><td>No </td><td> No </td></tr></tbody></table>


<h2>Class diagram</h2>

<img src='http://obvious.googlecode.com/svn-history/r306/images/obviousclass.png' />