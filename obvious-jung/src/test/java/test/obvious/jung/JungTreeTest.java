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

package test.obvious.jung;

import obvious.data.Edge;
import obvious.data.Node;
import obvious.data.Schema;
import obvious.data.Tree;
import obvious.jung.data.JungObviousTree;
import test.obvious.data.TreeTest;

/**
 * Implementation of  Tree test-case for JungObviousTree implementation.
 * @author Pierre-Luc Hemery
 *
 */
public class JungTreeTest extends TreeTest {

  @Override
  public Tree<Node, Edge> newInstance(Schema nSchema, Schema eSchema) {
    if (!eSchema.hasColumn("SRCNODE")) {
      eSchema.addColumn("SRCNODE", Integer.class, new Integer(0));
    }
    if  (!eSchema.hasColumn("DESTNODE")) {
      eSchema.addColumn("DESTNODE", Integer.class, new Integer(0));
    }
    return new JungObviousTree(nSchema, eSchema, "SRCNODE", "DESTNODE");
  }

}
