

#include "obvious/impl/TableImpl.hpp"
#include "obvious/impl/Column.hpp"

#include <algorithm>

namespace obvious {

  TableImpl::TableImpl()
  {
  }
  Schema * TableImpl::getSchema() const
  {
    return 0; // TODO
  }

  int TableImpl::getRowCount() const
  {
    return 0; // TODO
  }

  RowIterator TableImpl::beginRow() const
  {
    return RowIterator(0, this);
  }

  RowIterator TableImpl::endRow() const 
  {
    if (column_.size() == 0)
      return RowIterator(0, this);
    return RowIterator(column_[0]->size(), this);
  }
   
  int TableImpl::nextRow(int row) const
  {
    return row+1;
  }

  bool TableImpl::isValidRow(int rowId) const
  {
    return rowId < getRowCount();
  }

  int TableImpl::getFieldColumn(const std::string& field)
  {
    Map::iterator it = field_.find(field);
    if (it == field_.end())
      return -1;
    return it->second;
  }

  const std::string TableImpl::getValue(int rowId, const std::string& field)
  {
    return getValue(rowId, getFieldColumn(field));
  }

  const std::string TableImpl::getValue(int rowId, int col)
  {
    return ""; // TODO
  }
  
  bool TableImpl::isValueValid(int rowId, int col)
  {
    return true; // TODO
  }

  void TableImpl::beginEdit(int col) // throws ObviousException;
  {
    // TODO
  }

  bool TableImpl::endEdit(int col) // throws ObviousException;
  {
    return true; // TODO
  }

  bool TableImpl::isEditing(int col)
  {
    return false; // TODO
  }

  void TableImpl::addTableListener(TableListener * listnr)
  {
    if (listnr != 0)
      listeners_.push_back(listnr);
  }

  void TableImpl::removeTableListener(TableListener * listnr)
  {
    std::vector<TableListener *>::iterator it =
      std::find(listeners_.begin(), listeners_.end(), listnr);
    if (it != listeners_.end()) {
      listeners_.erase(it);
    }
  }

  std::vector<TableListener*> TableImpl::getTableListeners() const
  {
    return listeners_;
  }

  bool TableImpl::canAddRow() const
  {
    return true;
  }

  bool TableImpl::canRemoveRow() const
  {
    return true;
  }
  
  int TableImpl::addRow()
  {
    return 0; // TODO
  }

  int TableImpl::addRow(Tuple * tuple)
  {
    return 0; // TODO
  }

  bool TableImpl::removeRow(int row)
  {
    return false; //  TODO
  }

  void TableImpl::removeAllRows()
  {
    // TODO
  }

  void TableImpl::set(int rowId, const std::string& field, const std::string& val)
  {
    set(rowId, getFieldColumn(field), val);
  }

  void TableImpl::set(int rowId, int col, const std::string& val)
  {
    // TODO
  }

    void TableImpl::fireTableEvent(int start, int end, int col, int type)
    {
      // TODO
    }
  
} // namespace obvious
