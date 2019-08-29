package com.project.netty.exception;

public class AppException extends RuntimeException
{
	private static final long serialVersionUID = 6144373918049944913L;
 
public AppException(String msg)
  {
    super(msg);
  }

  public AppException(String msg, Throwable t)
  {
    super(msg, t);

    setStackTrace(t.getStackTrace());
  }
}