package obvious;

/**
 * Class Predicate
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public interface Predicate {
    boolean apply(Table table, int rowId);
}
