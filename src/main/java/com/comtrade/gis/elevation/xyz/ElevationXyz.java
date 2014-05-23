package com.comtrade.gis.elevation.xyz;

import java.util.ArrayList;
import java.util.List;

public class ElevationXyz {

  /**
   * Horizontal and vertical resolution of DMV5 model is 5m.
   */
  private static final int DMV5_RES = 5;

  /**
   * The Coordinate Reference System for data, described as EPSG code.
   */
  private String crs;

  /**
   * Data described as list of x, y, z points. Data is not ordered. It is split
   * in multiple blocks. It can not be read sequentially as lines or columns of
   * points.
   */
  private List<Xyz> data;

  /**
   * Geospatial data normalized to raster with pixel values used as elevation.
   */
  private short[][] rasterData;

  /**
   * Minimal X - longitude value.
   */
  private double minX = Integer.MAX_VALUE;

  /**
   * Minimal Y - latitude value.
   */
  private double minY = Integer.MAX_VALUE;

  /**
   * Max X - longitude value.
   */
  private double maxX;

  /**
   * Max Y - longitude value.
   */
  private double maxY;

  /**
   * Last value of X - longitude.
   */
  private double lastX;

  /**
   * Last value of Y - latitude.
   */
  private double lastY;

  protected void calculateBounds() {
    for (Xyz p : data) {
      evalBounds(p.getX(), p.getY());
    }
  }

  /**
   * Eval bounds, cumulatively evaluates and finds, max an min values for 2D
   * point.
   * 
   * @param x
   *          X or longitude value
   * @param y
   *          Y or latitude value
   */
  private void evalBounds(double x, double y) {

    if (x > maxX) {
      maxX = x;
    }
    if (y > maxY) {
      maxY = y;
    }
    if (x < minX) {
      minX = x;
    }
    if (y < minY) {
      minY = y;
    }
    lastX = x;
    lastY = y;
  }

  /**
   * Normalize geospatial data to image data that can be used for display. <br>
   * Before running normalize data has to be read and bounds evaluated.
   */
  public void normalize() {
    // Convert geospatial data to matrix of values that are 0,0
    // based and each value represents resolution of 5m.
    // Actually this is pixel image with rows and columns.
    double xWidth = maxX - minX;
    double yHight = maxY - minY;
    int xRes = (int) (xWidth / DMV5_RES) + 1;
    int yRes = (int) (yHight / DMV5_RES) + 1;
    rasterData = new short[xRes][yRes];
    // Pre-populate matrix with missing data value
    for (int x = 0; x < xRes; x++) {
      for (int y = 0; y < yRes; y++) {
        rasterData[x][y] = 0;
      }
    }
    // Fill matrix with normalized elevation data
    for (Xyz p : data) {
      int iX = (int) ((p.getX() - minX) / DMV5_RES);
      int iY = (int) ((p.getY() - minY) / DMV5_RES);
      rasterData[iX][iY] = (short)Math.round(p.getAlt()); // round elevation to full m
    }
  }

  public String getCrs() {
    return crs;
  }

  public void setCrs(String crs) {
    this.crs = crs;
  }

  public List<Xyz> getData() {
    return data;
  }

  public void setData(List<Xyz> data) {
    this.data = data;
  }

  /**
   * Adds the data point to list of data.
   * 
   * @param xyz
   *          the xyz
   */
  public void addData(Xyz xyz) {
    if (this.data == null) {
      this.data = new ArrayList<Xyz>();
    }

    this.data.add(xyz);
  }

  public double getMinX() {
    return minX;
  }

  public void setMinX(double minX) {
    this.minX = minX;
  }

  public double getMinY() {
    return minY;
  }

  public void setMinY(double minY) {
    this.minY = minY;
  }

  public double getMaxX() {
    return maxX;
  }

  public void setMaxX(double maxX) {
    this.maxX = maxX;
  }

  public double getMaxY() {
    return maxY;
  }

  public void setMaxY(double maxY) {
    this.maxY = maxY;
  }

  public short[][] getRasterData() {
    return rasterData;
  }

}
