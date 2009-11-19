/*
* Copyright (c) 2009, INRIA
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

package obvious.data;

import java.util.Collection;

/**
 * Interface Tree.
 *
 * @param <V> Vertex object
 * @param <E> Edge object
 *
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Tree<V, E> extends Forest<V, E> {

    /**
     * Gets depth of a node.
     * @param node to inspect
     * @return depth of the node
     */
    int getDepth(V node);

    /**
     * Gets height of the tree.
     * @return the height of the tree.
     */
    int getHeight();

    /**
     * Gets the root of the tree.
     * @return root node
     */
    V getRoot();

    /**
     * Gets the parent node of a node.
     * @param node child node
     * @return parent node
     */
    V getParentNode(V node);

    /**
     * Gets the parent edge of a node.
     * @param node child node
     * @return parent edge
     */
    E getParentEdge(V node);

    /**
     * Gets all the child nodes of a node.
     * @param node parent node
     * @return collection of child nodes
     */
    Collection<V> getChildNodes(V node);

    /**
     * Gets all the child edges of a node.
     * @param node parent node
     * @return collection of child edges
     */
    Collection<E> getChildEdges(V node);
//    public int getChildCount(V node);
}