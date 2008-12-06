package obvious.scale;

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
   
    /** A string indicating the type of scale this is. */
    String getScaleType();
   
    /** The minimum data value backing this scale. Note that the actual
     *  minimum scale value may be lower if the scale is not flush. */
    Object getMin();
    void setMin(T min);
   
    /** The maximum data value backing this scale. Note that the actual
     *  maximum scale value may be higher if the scale is not flush. */
    Object getMax();
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
