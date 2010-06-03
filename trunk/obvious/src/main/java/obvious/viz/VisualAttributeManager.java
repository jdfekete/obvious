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

import java.util.HashMap;
import java.util.Map;

import obvious.data.Tuple;

/**
 * This class is used to manage visual attributes in Obvious implementations.
 * A list of well-known visual attributes is defined, and each one is designed
 * by an alias. This alias will be used to access in each implementation to the
 * real visual field. A map is used to perform this operation. For those well
 * known attributes, getters function are available.
 * However, it is possible for the user to add "custom" visual attribute with
 * the method addVisualAttribute. The standard method getVisualAttributeAt
 * should be used to retrieve the value of the attribute in a visual table.
 * @author Hemery
 *
 */
public abstract class VisualAttributeManager {

  /**
   * Map of alias.
   */
  private Map<String, String> aliasVisAttribute = new HashMap<String, String>();

  /**
   * Alias for visual attribute color.
   */
  public static final String VISUAL_COLOR = "color_attribute";

  /**
   * Alias for visual attribute shape.
   */
  public static final String VISUAL_SIZE =  "shape_attribute";

  /**
   * Alias for visual attribute validated.
   */
  public static final String VISUAL_VALIDATED = "validated_attribute";

  /**
   * Alias for visual attribute shape.
   */
  public static final String VISUAL_SHAPE = "shape_attribute";

  /**
   * Alias for visual attribute label.
   */
  public static final String VISUAL_LABEL = "label_attribute";

  /**
   * Alias for visual attribute x coordinate.
   */
  public static final String VISUAL_X = "x_label";

  /**
   * Alias for visual attribute y_coordinate.
   */
  public static final String VISUAL_Y = "y_label";

  /**
   * Constructor.
   */
  public VisualAttributeManager() {
    initAliasMap();
  }

  /**
   * Fills the alias map with convenient values for the current implementation.
   */
  public abstract void initAliasMap();

  /**
   * Gets the alias map.
   * @return alias map
   */
  public Map<String, String> getAliasMap() {
    return this.aliasVisAttribute;
  }

  /**
   * Adds a custom visual attribute to the visual attribute map.
   * @param alias alias for the attribute to add
   * @param field field corresponding to the alias
   * @return true if added, else false
   */
  public boolean addVisualAttribute(String alias, String field) {
    if (!aliasVisAttribute.containsKey(alias)) {
      aliasVisAttribute.put(alias, field);
      return true;
    }
    return false;
  }

  /**
   * Gets the value of the Size visual attribute for a given row.
   * @param tuple an obvious tuple
   * @return value of the Size visual attribute
   */
  public int getColor(Tuple tuple) {
    return (Integer) getAttributeValuetAt(tuple, aliasVisAttribute.get(
        VISUAL_COLOR));
  }

  /**
   * Gets the value of the Size visual attribute for a given row.
   * @param tuple an obvious tuple
   * @return value of the Size visual attribute
   */
  public double getSize(Tuple tuple) {
    return (Double) getAttributeValuetAt(tuple, aliasVisAttribute.get(
        VISUAL_SIZE));
  }

  /**
   * Gets the value of the Validated visual attribute for a given row.
   * @param tuple an obvious tuple
   * @return value of the Validated visual attribute
   */
  public boolean getValidated(Tuple tuple) {
    return (Boolean) getAttributeValuetAt(
        tuple, aliasVisAttribute.get(VISUAL_VALIDATED));
  }

  /**
   * Gets the value of the Shape visual attribute for a given row.
   * @param tuple an obvious tuple
   * @return value of the Shape visual attribute
   */
  public int getShape(Tuple tuple) {
    return (Integer) getAttributeValuetAt(tuple, aliasVisAttribute.get(
        VISUAL_SHAPE));
  }

  /**
   * Gets the value of the Label visual attribute for a given row.
   * @param tuple an obvious tuple
   * @return value of the Label visual attribute
   */
  public String getLabel(Tuple tuple) {
    return (String) getAttributeValuetAt(tuple, aliasVisAttribute.get(
        VISUAL_LABEL));
  }

  /**
   * Gets the value of the X visual attribute for a given row.
   * @param tuple an obvious tuple
   * @return value of the X visual attribute
   */
  public double getX(Tuple tuple) {
    return (Double) getAttributeValuetAt(tuple, aliasVisAttribute.get(
        VISUAL_X));
  }

  /**
   * Gets the value of the Y visual attribute for a given row.
   * @param tuple an obvious tuple
   * @return value of the Y visual attribute
   */
  public double getY(Tuple tuple) {
    return (Double) getAttributeValuetAt(tuple, aliasVisAttribute.get(
        VISUAL_Y));
  }

  /**
   * Returns the attribute at the row indicated by the row index.
   * @param tuple an obvious tuple
   * @param alias an existing alias
   * @return value of the alias for this column
   */
  public abstract Object getAttributeValuetAt(Tuple tuple, String alias);

}
