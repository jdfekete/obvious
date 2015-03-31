# Introduction #

The project described in this page concerns the port of Vistrails into Java with the help of Jython (a Python implementation running on a Java virtual machine).

# Steps #

**19 OCTOBER 2011** Graphics part of the pipeline view have updated. After the execute button has been clicked, modules are coloured based on their execution status. For the moment, since execution is not totally working in jython, all modules are not executed and then coloured in red.

http://obvious.googlecode.com/svn/VAST2011/figures/screenVistrailspipelinecolor.PNG


**9 OCTOBER 2011** Layout now shows ports in the pipeline view and connections are drawn between ports. Nodes and edges of the different graphs are drawn in a nicer way. Fixes minor problems in the layout.

Improvments can be seen on the screenshot below:

http://obvious.googlecode.com/svn/VAST2011/figures/screenVistrails2.PNG

**29 SEPTEMBER 2011** Updated layout.

http://obvious.googlecode.com/svn/VAST2011/figures/screenVistrails.PNG

**27 SEPTEMBER 2011** The java branch of vistrails has been merged with the core\_no\_gui branch (vistrails 2.0 with Qt reference deleted from core).

**26 SEPTEMBER 2011** A clicked node in the version view is now highlighted.

**21 SEPTEMBER 2011** The interface has been polished and updated: with buttons it is possible to switch between different views and when a node is clicked on the version tree, the pipeline view displays the corresponding pipeline view for this version.

**15 SEPTEMBER 2011** : Pipeline tree can now be displayed in Jython/Swing. Next step is to assemblate and polish everything into one JFrame. Then, add some interactivity with module (launch and additional panels).

http://obvious.googlecode.com/svn/VAST2011/figures/PipelineTree.PNG

**14 SEPTEMBER 2011** : Building the pipeline tree from the pipeline in order to use the same graph layout as the version tree. For the moment, the layout complains about a parent/child problems (probably undefined root for the tree)... more to come

**13 SEPTEMBER 2011** : Displaying the version tree with Jython/Swing. It uses the Vistrail tree layout already written in the PythonC version of vistrails. Coordinates are then used by a Java graphics to draw rect (nodes) and line (edges).

**END OF JULY 2011** : Units tests for the Vistrails core have been run on Jython. Test failed or returned errors when they miss the QT dependancy in Jython, or some specific functions of PythonC missing in Jython.

```
Ran 186 tests in 2.422s

FAILED (failures=3, errors=56)
```

**MID-JULY 2011** : Project starts.