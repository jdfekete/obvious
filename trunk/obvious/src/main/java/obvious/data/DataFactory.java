package obvious.data;

import javax.xml.parsers.DocumentBuilder;

import obvious.ObviousException;

/**
 * Class DataFactory
 * 
 * @author Jean-Daniel Fekete
 * @version $Revision$
 */
public abstract class DataFactory {
    static private DataFactory instance;
    
    protected DataFactory() {}
    
    public static DataFactory getInstance() throws ObviousException {
        if (instance == null) {
            String className = System.getProperty("obvious.DataFactory");
            if (className == null) {
                throw new ObviousException("Property obvious.DataFactory not set");
            }
            try {
                Class c = Class.forName(className);
                instance = (DataFactory)c.newInstance();
            }
            catch(Exception e) {
                throw new ObviousException(e);
            }
        }
        return instance;
    }
    
    public abstract Table createTable(String name, Schema schema) throws ObviousException;
    public abstract Table wrapTable(Object unerlyingTable) throws ObviousException;
    public abstract Network createGraph(String name, Schema nodeSchema, Schema edgeSchema) throws ObviousException;
    public abstract Network wrapGraph(Object underlyingGraph) throws ObviousException;
    
}
