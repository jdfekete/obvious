# Introduction #

A Predicate is an interface providing the method `boolean apply(Table table, int rowId)`. The obvious implementation **obvious-prefuse** includes an implementation of the Predicate working with all realizations of the Table interface.


# Implementations #

## obvious-prefuse ##

In obvious-prefuse implementation, the PrefuseObviousPredicate provides a realization of Predicate interface. **All** obvious table instances can be used with this wrapper of prefuse's predicate.

To create a predicate, you can wrap an existing prefuse predicate or provide to the constructor a valid prefuse expression (String). Prefuse expression's grammar is explained [the javadoc page of prefuse class Expression parser](http://prefuse.org/doc/api/prefuse/data/expression/parser/ExpressionParser.html). The following examples are taken from obvious-examples (_obvious.demo.predicate.PredicateDemo.java_) :

```
    // Create the predicate by wrapping an existing predicate
    String predString = "value < 5";
    prefuse.data.expression.Predicate p = (prefuse.data.expression.Predicate)
        ExpressionParser.parse(predString);
    Predicate pred = new PrefuseObviousPredicate(p);
```

```
    // Directly create the obvious predicate
    Predicate pred = new PrefuseObviousPredicate("value < 5");
```

## obvious-ivtk ##

To be implemented.