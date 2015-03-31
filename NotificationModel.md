# Introduction #

Obvious as many popular InfoVis toolkits such as Prefuse, Infovis Toolkit and JUNG. With this patterns, data structures are **Observable** meaning that some **Observers** can be registered in order to be notified when structures are changed.

However, during the design and conception of Obvious bindings, we saw that each toolkit implements this model in its own way with its own syntax and idioms. That is why Obvious uses a notification made to handle a wide variety of notification models even those that are not currently implemented in toolkits. For example, we design a model based on the Obvious-JDBC binding supporting commit and batch mechanisms.

# A standard syntax for the notification model #

All implementations of the Table interface in Obvious have methods to register, unregister, and get listeners:

```
table.addTableListener(listener);
table.removeTableListener(listener);
```

Thus, when a method that changes the structure is called (addRow, removeRow, set), the _fireTableEvent_ (or _fireNetworkEvent_) method is called. Then, this method awakes all registered listeners and furnishes them information about changes:

```
// start : starting row for change, end : ending row, col : columns
// affected by the changes, type : type of the operation (add, rem...)
fireTableEvent(start, end, col, type);
```

In the listener, the method tableChanged is called, with the arguments provided by the _fireTableEvent_ method.

# Obvious JDBC binding: introducing new mechanisms for notification model #

Since one operation occuring on the data model can have an impact on a large number of rows and then generates a lot of notifications. Thus, it  is followed, for example, by updating a large number of visual items causing the application to be unresponsive for a long time.

Obvious introduces new mechanisms to handle transaction (commit) and batches with the beginEdit/endEdit system. When the beginEdit method is called on the data structure, the listener's beginEdit method is called to launch a batch transaction. It is possible with a  mode argument of the beginEdit method to specify if the transaction is atomic or only batched. Several strategies have been implemented to support the beginEdit mechanism:

  * _Lazy strategy_ : after a beginEdit, Observers ignore all calls to tableChanged method untils the endEdit method is called. After, the Observer's action are performed (layout is recomputed)
  * _Batch strategy_ : after beginEdit, the observer buffers the information sent by tableChanged. When endEdit is called, the actions are performed on each of the buffered items
  * _Transaction strategy_ same as the batch strategy but when endEdit is called, some structural invariants are check (for example, checks if an integer only contains even numbers) before performing actions.

# Sequence diagramm for the notification model #

![http://obvious.googlecode.com/svn/VAST2011/figures/notification.png](http://obvious.googlecode.com/svn/VAST2011/figures/notification.png)