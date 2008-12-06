package obvious.query;

import obvious.Tuple;

/**
 * Abstarct base class for a Literal Expression that evaluates to a
 * constant value.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class Literal extends AbstractExpression {

    /**
     * Evaluate the given tuple and data field and return the
     * result as a new Literal instance.
     * @param t the Tuple
     * @param field the data field to lookup
     * @return a new Literal expression containing the
     * value of the Tuple's data field
     */
    public static Literal getLiteral(Tuple t, String field) {
        Class type = t.getColumnType(field);
        if ( type == int.class )
        {
            return new NumericLiteral(t.getInt(field));
        }
        else if ( type == long.class )
        {
            return new NumericLiteral(t.getLong(field));
        }
        else if ( type == float.class )
        {
            return new NumericLiteral(t.getFloat(field));
        }
        else if ( type == double.class )
        {
            return new NumericLiteral(t.getDouble(field));
        }
        else if ( type == boolean.class )
        {
            return new BooleanLiteral(t.getBoolean(field));
        }
        else
        {
            return new ObjectLiteral(t.get(field));
        }
    }
    
    /**
     * Return the given object as a new Literal instance.
     * @param val the object value
     * @return a new Literal expression containing the
     * object value. The type is assumed to be the
     * value's concrete runtime type.
     */
    public static Literal getLiteral(Object val) {
        return getLiteral(val, val.getClass());
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
     * Return the given object as a new Literal instance.
     * @param val the object value
     * @param type the type the literal should take
     * @return a new Literal expression containing the
     * object value
     */
    public static Literal getLiteral(Object val, Class type) {
        if ( isNumericType(type) )
        {
            return new NumericLiteral(val);
        }
        else if ( type == boolean.class )
        {
            return new BooleanLiteral(((Boolean)val).booleanValue());
        }
        else
        {
            if ( type.isInstance(val) ) {
                return new ObjectLiteral(val);
            } else {
                throw new IllegalArgumentException("Object does "
                        + "not match the provided Class type.");
            }
        }
    }
    
} // end of abstarct class Literal
