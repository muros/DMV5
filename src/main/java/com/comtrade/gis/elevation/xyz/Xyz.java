package com.comtrade.gis.elevation.xyz;

/**
 * Object containing 2.5 Dimensional data.
 * Lat / Lon / Alt
 * 
 * @author muros
 *
 */
public class Xyz {
  
  /** 
   * X or longitude.
   */
  private double x;
  
  /** 
   * Y or latitude. 
   */
  private double y;
  
  /**
   * Altitude.
   */
  private double alt;

  /**
   * Triple constructor.
   * 
   * @param x longitude
   * @param y latitude
   * @param z altitude
   */
  public Xyz(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.alt = z;
  }
  
  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getAlt() {
    return alt;
  }

  public void setAlt(double hight) {
    this.alt = hight;
  }
}
