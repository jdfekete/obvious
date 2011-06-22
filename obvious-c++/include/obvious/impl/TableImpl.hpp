/*
* Copyright (c) 2011, INRIA
* All rights reserved.
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright
*       notice, this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of INRIA nor the names of its contributors may
*       be used to endorse or promote products derived from this software
*       without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
* EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
#ifndef __obviousTableImpl_hpp
#define __obviousTableImpl_hpp

#include <vector>
#include <map>

#include "obvious/data/Table.hpp"

namespace obvious
{

  class Column;

  class TableImpl : public Table
  {
  public:
    TableImpl();
    virtual Schema * getSchema() const;
    virtual int getRowCount() const;
    virtual RowIterator beginRow() const;
    virtual RowIterator endRow() const;
    virtual int nextRow(int row) const;
    virtual bool isValidRow(int rowId) const;
    virtual const std::string getValue(int rowId, const std::string& field);
    virtual const std::string getValue(int rowId, int col);
    virtual bool isValueValid(int rowId, int col);
    virtual void beginEdit(int col); // throws ObviousException;
    virtual bool endEdit(int col); // throws ObviousException;
    virtual bool isEditing(int col);
    virtual void addTableListener(TableListener * listnr);
    virtual void removeTableListener(TableListener * listnr);
    virtual std::vector<TableListener*> getTableListeners() const;
    virtual bool canAddRow() const;
    virtual bool canRemoveRow() const;
    virtual int addRow();
    virtual int addRow(Tuple * tuple);
    virtual bool removeRow(int row);
    virtual void removeAllRows();
    virtual void set(int rowId, const std::string& field, const std::string& val);
    virtual void set(int rowId, int col, const std::string& val);
    virtual void fireTableEvent(int start, int end, int col, int type);

    int getFieldColumn(const std::string& field);
  private:
    typedef std::map<std::string,int> Map;
    std::vector<Column*> column_;
    Map field_;
    std::vector<TableListener*> listeners_;
  };

} // end namespace obvious

#endif
