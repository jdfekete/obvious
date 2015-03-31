# Introduction #

This page presents the roadmap for the ongoing Vistrails-Jython project.

# To do #

-) **Making information panel for module in order to set up value and attributes.**

I suppose this step can be done later since I think we can manually assign some values to the different modules (i.e. string modules for the moment). Thus, we can concentrate on more fundamental steps.

![http://obvious.googlecode.com/svn/VAST2011/figures/moduleinfopanel.png](http://obvious.googlecode.com/svn/VAST2011/figures/moduleinfopanel.png)

-) **Executing a simple workflow on Jython**

For instance, the aims is to run the current example workflow with concatenation of strings. To finish this step, we need: 1) Assign values to modules 2) Correctly call exec\_current\_pipeline method 3) Display the result in a console.

-) **Updating the pipeline view code**: when a workflow is executed the state of execution of each module is indicated by a color in the pipeline view.

I suppose this is done with Qt in the standard version of Vistrails. That is why we need to rewrite that part in Swing probably and also to be able to be notified in Jython when a module has a state change.

![http://obvious.googlecode.com/svn/VAST2011/figures/executedworkflow.png](http://obvious.googlecode.com/svn/VAST2011/figures/executedworkflow.png)

-) **Writing a module using Obvious, building and running a workflow with it.**


It can be something very simple at the beginning: we can combine standard vistrails modules (such as String, Concatenation, Httpsource...) and an Obvious module that takes in entry node names as string or a file describing a graph structure (graphml, csv) and that returns an Obvious Network object.
It can be later analyzed, displayed (in a Frame or as text in a console) with another module such as StandardOutput. If we can execute simple workflows on Jython, I supose this part won't be really difficult to achieve.

A possible schema for an Obvious-vistrails workflow is given above:

![http://obvious.googlecode.com/svn/VAST2011/figures/hypotheticalobviousworkflow.png](http://obvious.googlecode.com/svn/VAST2011/figures/hypotheticalobviousworkflow.png)

-) **Adding edition panel to edit existing workflows and to create new ones**

The first part of this step is purely a Swing one: creating panel with the list of available module and (un)drawing on the pipeline view new or deleted modules.

The other part is purely vistrails, we have to be sure that changes done with the GUI are saved into the vistrails file when asked.

# Done #

More details about progress made into the development of the java branch can be found [here](JavaVistrails.md) (detailled steps and screenshots).

-) Displaying module ports on the pipeline view

-) Instantiating correctly package manager and module registry on jython

-) Linking the version view and the pipeline view

-) Merging the java branch with the core no-qt branch

-) Merging the java branch with the 2.0 branch

-) Making a core with no-qt references

-) Displaying pipeline and version views on Swing with
Jython

-) Writing a simple layout based on Java graphics for pipeline and version views

-) Reading vistrails files on Jython

-) Making a core with no-qt references

-) Creating a java branch on git

-) Executing tests on the core