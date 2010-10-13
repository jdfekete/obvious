/*
* Copyright (c) 2010, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package obvious.demo.ivtk;

import javax.swing.JFrame;

import infovis.io.AbstractReader;
import infovis.tree.DefaultTree;
import infovis.tree.io.TreeReaderFactory;
import obvious.data.Tree;
import obvious.ivtk.data.IvtkObviousTree;
import obvious.ivtk.view.IvtkObviousView;
import obvious.ivtk.viz.util.IvtkTreeMapVis;
import obvious.view.JView;
import obvious.viz.Visualization;

/**
 * Tree map example for obvious-ivtk.
 * @author Hemery
 *
 */
public class TreeMapExample {

    /**
     * An obvious JView.
     */
    private JView view;

    /**
     * Constructor.
     */
    public TreeMapExample() {
        /*
         * Creates the data structure. For this example, the election
         * TreeMap from ivtk is wrapped into an ObviousTree structure.
         */
        String file = "src/main/resources/election.tm3";
        DefaultTree t = new DefaultTree();
        AbstractReader reader =
            TreeReaderFactory.createTreeReader(file, t);
        reader.load();
        Tree tree = new IvtkObviousTree(t);
        /*
         * Creates the associated visualization.
         */
        Visualization vis = new IvtkTreeMapVis(tree, null, null, null);
        /*
         * Creates the associated view.
         */
        view = new IvtkObviousView(vis, null, null, null);
    }

    /**
     * Gets the Obvious view associated to the tree map.
     * @return an obvious JView instance
     */
    public JView getJView() {
        return this.view;
    }

    /**
     * Demo method.
     */
    public static void demo() {
        TreeMapExample treeMapEx = new TreeMapExample();
        JFrame frame = new JFrame("TreeMap example (Election)"
                + "from ivtk based on Obvious");
        frame.add(treeMapEx.getJView().getViewJComponent());
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Main method.
     * @param args arguments of the main
     */
    public static void main(String[] args) {
        demo();
    }
}
