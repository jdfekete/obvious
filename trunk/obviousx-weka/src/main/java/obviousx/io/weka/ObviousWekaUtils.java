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

package obviousx.io.weka;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ObviousWekaUtils {

  public static boolean isNumeric(Class<?> c) {
    return (c.equals(int.class) || c.equals(Integer.class) ||
        c.equals(double.class) || c.equals(Double.class) ||
        c.equals(float.class) || c.equals(Float.class) ||
        c.equals(short.class) || c.equals(Short.class) ||
        c.equals(long.class) || c.equals(Long.class) ||
        c.equals(BigDecimal.class) || c.equals(BigInteger.class));
  }
  
  public static boolean isString(Class<?> c) {
    return (c.equals(String.class));

  }
  
  public static boolean isRelational(Class<?> c) {
    return false;
  }
  
  public static boolean isDate(Class<?> c) {
    return (c.equals(Date.class));

  }
  
  public static boolean isNominal(Class<?> c) {
    if (c.equals(Enum.class)) {
      return true;
    }
    return false;
  }
   
}
