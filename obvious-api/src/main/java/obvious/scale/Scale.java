package obvious.scale;

/**
 * The Scale objects is used to map column values to position along an
 * axis for visualizations such as scatter plots, time series, box plots, etc. and to
 * get the tick marks too called "label values" below.
 * 
 * Usage scenario: painting 10 tick marks for a scatterplot that contain Float values
 * for the X axis using a linear scale. yPosition is the baseline where the axis marks are
 * drawn.
 * 
 * FIXME JDF: this is a possible way to explain how to use it. There are two ways to
 * specify the number of marks you want: a fixed count or the max size of a mark and the
 * available size (dividing the second by the first gives a count). 
 * When the window is resized, the count has to change so this interface
 * implies that a new scale is recreated at each paint, which makes the setMin and
 * setMax methods pretty useless. If Scales are reusable, we should add a setCount(int)
 * method. If not, we can remove the setMim(T) and setMax(T) and leave it to the
 * constructor.  I would add a setCount(int) method.
 * 
 * Scale<Float> linScale = new LinearScale<Float>(10); // specifies how many marks are desired.
 * linScale.setMin(minValue);
 * linscale.setMax(maxValue);
 * 
 * for (Float mark : linScale.values()) {
 *      double d = linScale.interpolate(mark);
 *      drawStringCentered(d*getWidth(), yPosition, mark.toString());
 * } 
 * 
 */
public interface Scale<T> {

	/**
     * Flag indicating if the scale bounds should be flush with the data.
     * If true, the scale should be flush with the data range, such that
     * the min and max values should sit directly on the extremes of the
     * scale. If false, the scale should be padded as needed to make the
     * scale more readable and human-friendly.
     */
    boolean isFlush();
    void setFlush(boolean flush);
   
    /** A string indicating the type of scale this is, such as "linear" or "log'. */
    String getScaleType();
   
    /** The minimum data value backing this scale. Note that the actual
     *  minimum scale value may be lower if the scale is not flush. */
    T getMin();
    void setMin(T min);
   
    /** The maximum data value backing this scale. Note that the actual
     *  maximum scale value may be higher if the scale is not flush. */
    T getMax();
    void setMax(T max);
   
    /**
     * Returns an interpolation fraction indicating the position of the input
     * value within the scale range.
     * @param value a data value for which to return an interpolation
     *  fraction along the data scale
     * @return the interpolation fraction of the value in the data scale
     */
    double interpolate(T value);

    /**
     * Performs a reverse lookup, returning an object value corresponding
     * to a interpolation fraction along the scale range.
     * @param f the interpolation fraction
     * @return the scale value at the interpolation fraction. May return
     *  null if no value corresponds to the input fraction.
     */
    T lookup(double f);

    /**
     * Returns a set of label values for this scale.
     * @param num a desired target number of labels. This parameter is
     *  handled idiosyncratically by different scale sub-classes.
     * @return an array of label values for the scale
     */
    Iterable<T> values();
	
}
