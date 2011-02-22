/*
* Copyright (c) 2010, INRIA
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

package obviousx;

/**
 * A Runtime Exception for Obvious toolkit.
 * @author Pierre-Luc Hemery
 *
 */
public class ObviousxRuntimeException extends RuntimeException {

  /**
   * Serial VersionUID.
   */
  private static final long serialVersionUID = 5599256180371198371L;

  /**
   * Constructs a new ObviousRuntimeException.
   */
  public ObviousxRuntimeException() {
    super();
  }

  /**
   * Constructs a new ObviousRuntimeException with the input message.
   * @param message exception message
   */
  public ObviousxRuntimeException(String message) {
    super(message);
  }

  /**
   * Constructs a new ObviousRuntimeException with the input cause.
   * @param cause exception cause
   */
  public ObviousxRuntimeException(Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new ObviousRuntimeException with the input cause and message.
   * @param message exception message
   * @param cause exception cause
   */
  public ObviousxRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

}