

#include "obvious/util/SmartPointer.hpp"

namespace obvious {

/**
 * Increase the reference count (mark as used by another object).
 */
void 
SmartPointed::Register() const
{
  //TODO protected for concurrent use
  refcount_++;
}


/**
 * Decrease the reference count (release by another object).
 */
void 
SmartPointed::UnRegister() const
{
  int tmpReferenceCount = --refcount_;
  if ( tmpReferenceCount <= 0) {
    delete this;
  }
}


} // namespace obvious
