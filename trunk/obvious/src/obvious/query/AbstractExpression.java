package obvious.query;

import java.util.ArrayList;

import obvious.Tuple;

/**
 * Abstract base class for Expression implementations. Provides support for
 * listeners and defaults every Expression evaluation method to an
 * unsupported operation.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractExpression
    implements Expression, ExpressionListener
{
    private ArrayList m_listeners = new ArrayList();
    
    /**
     * @see prefuse.data.expression.Expression#visit(prefuse.data.expression.ExpressionVisitor)
     */
    public void visit(ExpressionVisitor v) {
        v.visitExpression(this);
    }

    /**
     * @see prefuse.data.expression.Expression#addExpressionListener(prefuse.data.event.ExpressionListener)
     */
    public final void addExpressionListener(ExpressionListener lstnr) {
        if ( !m_listeners.contains(lstnr) ) {
            m_listeners.add(lstnr);
            addChildListeners();
        }
    }
    
    /**
     * @see prefuse.data.expression.Expression#removeExpressionListener(prefuse.data.event.ExpressionListener)
     */
    public final void removeExpressionListener(ExpressionListener lstnr) {
        m_listeners.remove(lstnr);
        if ( m_listeners.size() == 0 )
            removeChildListeners();
    }

    /**
     * Indicates if any listeners are registered with this Expression.
     * @return true if listeners are registered, false otherwise
     */
    protected final boolean hasListeners() {
        return m_listeners != null && m_listeners.size() > 0;
    }
    
    /**
     * Fire an expression change.
     */
    protected final void fireExpressionChange() {
        Object[] lstnrs = m_listeners.toArray();
        for ( int i=0; i<lstnrs.length; ++i ) {
            ((ExpressionListener)lstnrs[i]).expressionChanged(this);
        }
    }
    
    /**
     * Add child listeners to catch and propagate sub-expression updates.
     */
    protected void addChildListeners() {
        // nothing to do
    }

    /**
     * Remove child listeners for sub-expression updates.
     */
    protected void removeChildListeners() {
        // nothing to do
    }

    /**
     * Relay an expression change event.
     * @see prefuse.data.event.ExpressionListener#expressionChanged(prefuse.data.expression.Expression)
     */
    public void expressionChanged(Expression expr) {
        fireExpressionChange();
    }
    
    // ------------------------------------------------------------------------
    // Default Implementation
    
    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefuse.data.expression.Expression#get(prefuse.data.Tuple)
     */
    public Object get(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefuse.data.expression.Expression#getInt(prefuse.data.Tuple)
     */
    public int getInt(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefuse.data.expression.Expression#getLong(prefuse.data.Tuple)
     */
    public long getLong(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefuse.data.expression.Expression#getFloat(prefuse.data.Tuple)
     */
    public float getFloat(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefuse.data.expression.Expression#getDouble(prefuse.data.Tuple)
     */
    public double getDouble(Tuple t) {
        throw new UnsupportedOperationException();
    }

    /**
     * By default, throws an UnsupportedOperationException.
     * @see prefuse.data.expression.Expression#getBoolean(prefuse.data.Tuple)
     */
    public boolean getBoolean(Tuple t) {
        throw new UnsupportedOperationException();
    }
    

    
    /**
     * Indicates if a given class type is a primitive numeric one type
     * (one of byte, short, int, long, float, or double).
     * @param type the type to check
     * @return true if it is a primitive numeric type, false otherwise
     */
    public static boolean isNumericType(Class type) {
        return ( type == byte.class   || type == short.class ||
                 type == int.class    || type == long.class  || 
                 type == double.class || type == float.class );
    }
    
    /**
     * Get a compatible numeric type for two primitive numeric
     * class types. Any of (byte, short, int) will resolve to int.
     * @param c1 a numeric primitive class type (int, long, float, or double)
     * @param c2 a numeric primitive class type (int, long, float, or double)
     * @return the compatible numeric type for binary operations involving
     * both types.
     */
    public static Class getNumericType(Class c1, Class c2) {
        if ( !isNumericType(c1) || !isNumericType(c2) ) {
            throw new IllegalArgumentException(
                "Input types must be primitive number types");
        }
        if ( c1 == double.class || c2 == double.class ) {
            return double.class;
        } else if ( c1 == float.class || c1 == float.class ) {
            return float.class;
        } else if ( c1 == long.class || c2 == long.class ) {
            return long.class;
        } else {
            return int.class;
        }
    }
    
} // end of abstract class AbstractExpression
