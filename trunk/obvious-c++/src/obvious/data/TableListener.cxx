

#include "obvious/data/event/TableListener.hpp"

namespace obvious {
  
  TableListener::TableListener()
  {
  }

  void TableListener::beginEdit(int)
  {
  }

  bool TableListener::endEdit(int context)
  {
	  return true;
  }

  bool TableListener::checkInvariants()
  {
	  return true;
  }

  void TableListener::tableChanged(Table *, int, int, int, int)
  {
  }

} // namespace obvious
