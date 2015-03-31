# Introduction #

Often when building an application, you already have an existing data model and you want to easily link it with existing ones to benefit of their features. Since Obvious has been designed to encapsulate a large variety of data models, linking an existing data model to is something easy to do.

Two options exists : you can load your data into an existing Obvious model, it will be described in the first part of this wiki article. The second, more elegant option, is explained in the last part of this article.

# Load your data model into Obvious #

The first thing to do to load an Obvious Model is to create its Schema. Schemas are used to store column/attribute informations of your data model. Thus, it contains for each colum/attribute the following fields :
name, type and default value.

Thus, let's assume, your data model is represented by an instance named mydatamodel and that it has method to retrieve informations about its columns, the Schema creation code should look like the following lines:

```
Schema schema = new SchemaImpl(); // a default schema used in Obvious

// Iterating over columns or attributes of your data model
for (int i = 0; i < mydatamodel.getColumnCount(); i=++) {
schema.addColumn(mydatamodel.getColumn(i).getName(),
    mydatamodel.getColumn(i).getType(), mydatamodel.getColumn(i).getDefaultValue());
}
```

Once the schema is ready, it is possible to create a Table, that will contained Tuple instances (rows). To facilitate the creation of tuples, Obvious introduced a generic tuple class TupleImpl that will act as container between your datamodel and the Obvious targetted model. The following lines explained how you should proceed to fill the Obvious table :

```
Table table = new ObviousChosenImplementationTable(schema);

for (Row row : datamodel.getRows()) {
   Object[] values = new Object[mydatamodel.getColumnCount());
   for (int i = 0; i < mydatamodel.getColumnCount(); i=++) {
      values[i] = row.get(i);
   }
   Tuple tuple = new TupleImpl(schema, values);
   table.addRow(tuple);
}
```

Once this is done, your model is now fully Obvious and you can enjoy all Obvious possibilities.

# Implementing Obvious with your data model #

Since Schema, Tuple and Table are interfaces, you can also implement it yourself and then wraps your data model.