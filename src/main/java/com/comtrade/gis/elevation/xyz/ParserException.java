package com.comtrade.gis.elevation.xyz;

/**
 * ParserException for exceptions during parsing of elevation
 * data file.
 */
public class ParserException extends Exception {

  /**
   * Instantiates a new parser exception.
   */
  public ParserException() {
  }
  
  /**
   * Instantiates a new parser exception with the message.
   *
   * @param message the message
   */
  public ParserException(String message) {
    super(message);
  }
  
  /* (non-Javadoc)
   * @see java.lang.Throwable#getMessage()
   */
  @Override
  public String getMessage() {
    return super.getMessage();
  }
}
