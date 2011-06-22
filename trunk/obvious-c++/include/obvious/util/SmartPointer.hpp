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
#ifndef __obviousSmartPointer_hpp
#define __obviousSmartPointer_hpp

namespace obvious
{

template <class T>
class SmartPointer 
{
public:
  
  /** Constructor  */
  SmartPointer () :
    ref_(0)
  {
  }

  /** Copy constructor  */
  SmartPointer (const SmartPointer<T> &p) :
    ref_(p.ref_)
  {
    this->Register();
  }
  
  /** Constructor to pointer p  */
  SmartPointer (T *p) :
    ref_(p)
  {
    this->Register();
  }
  
  /** Destructor  */
  ~SmartPointer ()
  {
    this->UnRegister();
    ref_ = 0;  
  }
  
  /** Overload operator ->  */
  T *operator -> () const
  {
    return ref_;
  }

  /** Return pointer to object.  */
  operator T * () const 
  {
    return ref_;
  }
  
  /** Test if the pointer has been initialized */
  bool IsNotNull() const
  {
    return ref_ != 0; 
  }

  bool IsNull() const
  {
    return ref_ == 0; 
  }

  /** Template comparison operators. */
  template <typename TR>
  bool operator == ( TR r ) const
  {
    return (ref_ == static_cast<const T*>(r) );
  }

  template <typename TR>
  bool operator != ( TR r ) const
  {
    return (ref_ != static_cast<const T*>(r) );
  }
    
  /** Access function to pointer. */
  T *GetPointer () const 
  {
    return ref_;
  }
  
  /** Comparison of pointers. Less than comparison.  */
  bool operator < (const SmartPointer &r) const
  {
    return (void*)ref_ < (void*) r.ref_;
  }
  
  /** Comparison of pointers. Greater than comparison.  */
  bool operator > (const SmartPointer &r) const
  {
    return (void*)ref_ > (void*) r.ref_;
  }

  /** Comparison of pointers. Less than or equal to comparison.  */
  bool operator <= (const SmartPointer &r) const
  {
    return (void*)ref_ <= (void*) r.ref_; 
  }

  /** Comparison of pointers. Greater than or equal to comparison.  */
  bool operator >= (const SmartPointer &r) const
  {
    return (void*)ref_ >= (void*) r.ref_;
  }

  /** Overload operator assignment.  */
  SmartPointer &operator = (const SmartPointer &r)
  {
    return this->operator = (r.GetPointer());
  }
  
  /** Overload operator assignment.  */
  SmartPointer &operator = (T *r)
  {
    if (ref_ != r)
      {
	T * tmp = ref_; //avoid recursive unregisters by retaining temporarily
	ref_ = r;
	this->Register();
	if ( tmp ) {
	  tmp->UnRegister();
	}
      }
    return *this;
  }
  
private:
  /** The pointer to the object referrred to by this smart pointer. */
  T* ref_;

  void Register()
  { 
    if(ref_) {
      ref_->Register();
    }
  }
  
  void UnRegister()
  {
    if(ref_) {
      ref_->UnRegister();
    }
  }
};

class SmartPointed
{
  /** Increase the reference count (mark as used by another object).  */
  virtual void Register() const;

  /** Decrease the reference count (release by another object).  */
  virtual void UnRegister() const;

  /** Gets the reference count on this object. */
  virtual int GetReferenceCount() const 
    {return refcount_;}

protected:
  SmartPointed(): refcount_(1)
  {
  }

  virtual ~SmartPointed(); 


private:
  mutable int refcount_;

  //TODO Later
  //  mutable SimpleFastMutexLock m_ReferenceCountLock;

};


} // namespace obvious

#endif

