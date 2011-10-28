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


package obvious.viz;

import java.util.ArrayList;
import java.util.Map;

import obvious.ObviousException;
import obvious.data.Network;
import obvious.data.Table;
import obvious.data.util.Predicate;


/**
 * Abstract Class VisualizationFactory. A factory that can create
 * {@link Visualization Visualization} instances. Each implementation of
 * Obvious that includes visualization part should implement this class.
 * Visualizations can be built from {@link Table Table} and
 * {@link Network Network}.
 *
 * @author  Hemery
 */
public abstract class VisualizationFactory {

  /**
   * Static instance of DataFactory.
   */
  private static VisualizationFactory instance;

  /**
   * Constructor.
   */
  protected VisualizationFactory() { }

  /**
   * Gets an instance of the convenient VisualizationFactory.
   * @return  an instance of VisualizationFactory
   * @throws ObviousException  if something bad happens
   * @uml.property  name="instance"
   */
  public static VisualizationFactory getInstance() throws ObviousException {
    if (instance == null) {
      String className = System.getProperty("obvious.VisualizationFactory");
      if (className == null) {
        throw new ObviousException(
                "Property obvious.VisualizationFactory not set");
      }
      try {
        Class<?> c = Class.forName(className);
        instance = (VisualizationFactory) c.newInstance();
      } catch (Exception e) {
        throw new ObviousException(e);
      }
    }
    return instance;
  }

  /**
   * Gets the list of all available visualizations for this factory.
   * @return the list of all available visualizations for this factory
   */
  public abstract ArrayList<String> getAvailableVisualization();

  /**
   * Creates an instance of Visualization.
   * @param table parent table
   * @param pred a predicate to filter the table
   * @param visName visualization technique name
   * @param param parameters of the visualization
   * @return an instance of Visualization
   */
  public abstract Visualization createVisualization(
      Table table, Predicate pred, String visName, Map<String, Object> param);
  /**
   * Creates an instance of Visualization.
   * @param network parent network
   * @param pred a predicate to filter the table
   * @param visName visualization technique name
   * @param param parameters of the visualization
   * @return an instance of Visualization
   */
  public abstract Visualization createVisualization(Network network,
      Predicate pred, String visName, Map<String, Object> param);

}
