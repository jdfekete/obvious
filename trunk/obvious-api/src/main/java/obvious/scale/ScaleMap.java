package obvious.scale;

public interface ScaleMap<T> {

	/**
     * Returns the x-coordinate corresponding to the lower end of the scale.
     * @return the x-coordinate for the minimum value
     */
    double getX1();
    
    /**
     * Returns the y-coordinate corresponding to the lower end of the scale.
     * @return the y-coordinate for the minimum value
     */
    double getY1();
    
    /**
     * Returns the x-coordinate corresponding to the upper end of the scale.
     * @return the x-coordinate for the maximum value
     */
    double getX2();
    
    /**
     * Returns the y-coordinate corresponding to the upper end of the scale.
     * @return the y-coordinate for the maximum value
     */
    double getY2();
    
    /**
     * Returns the scale value corresponding to a given coordinate.
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param stayInBounds if true, x,y values outside the current layout
     * bounds will be snapped to the bounds. If false, the value lookup
     * will attempt to extrapolate beyond the scale bounds. This value
     * is true by default.
     * @return the scale value corresponding to the given coordinate.
     */        
    T value(double x, double y, boolean stayInBounds);
    
    /**
     * Returns the x-coordinate corresponding to the given scale value
     * @param value the scale value to lookup
     * @return the x-coordinate at which that scale value is placed
     */       
    double X(T value);
   
    /**
     * Returns the y-coordinate corresponding to the given scale value
     * @param value the scale value to lookup
     * @return the y-coordinate at which that scale value is placed
     */
    double Y(T value);
	
}
