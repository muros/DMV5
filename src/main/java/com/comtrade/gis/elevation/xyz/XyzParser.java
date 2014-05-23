package com.comtrade.gis.elevation.xyz;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * The Class XyzParser provides parsing of Xyz data line by line. Line contains
 * single data point that is converted to Xyz point, that is also converted from
 * native to desired Coordinate Reference Sysem (CRS).
 * 
 * @author muros
 */
public class XyzParser {

  /**
   * The native CRS.
   */
  private CoordinateReferenceSystem srcCrs;

  /**
   * The destination CRS.
   */
  private CoordinateReferenceSystem destCrs;

  /**
   * The math transformation between src and dest CRS.
   */
  private MathTransform mathTransform;

  /**
   * Geometry factory.
   */
  private GeometryFactory gf;

  /**
   * Instantiates a new xyz parser. It requires native and destination CRS.
   * 
   * @param srcCrs
   *          the native CRS
   * @param destCrs
   *          the destination CRS.
   */
  public XyzParser(String srcCrs, String destCrs) throws ParserException {
    // It prepares geo transformation of coordinate system.
    try {
      this.srcCrs = CRS.decode(srcCrs);
      this.destCrs = CRS.decode(destCrs);
      this.mathTransform = CRS.findMathTransform(this.srcCrs, this.destCrs,
          true);
      this.gf = new GeometryFactory();
    } catch (NoSuchAuthorityCodeException e) {
      throw new ParserException("Error creating geo transformation.");
    } catch (FactoryException fe) {
      throw new ParserException("Error creating geo transformation.");
    }
  }

  /**
   * Parse line of data and convert it to triple of unmodified data. Data
   * contains longitude, latitude and altitude.
   * 
   * @param line
   *          raw line of data read from xyz file
   * @return triple of double values / unmodified
   * @throws ParserException
   *           in case of errorneous line format
   */
  public Xyz parseLine(String line) throws ParserException {
    // Parse line as triple of double values.
    double x, y, z;
    String[] tokens = line.split("\\s");

    if ((tokens == null) || (tokens.length != 3)) {
      throw new ParserException("Wrong elevation data line format.");
    }
    try {
      x = Double.parseDouble(tokens[0]);
      y = Double.parseDouble(tokens[1]);
      z = Double.parseDouble(tokens[2]);
    } catch (NumberFormatException e) {
      throw new ParserException("Wrong number in line " + line);
    }

    return new Xyz(x, y, z);
  }

  /**
   * Conversion of coordinate from D48 to D96.
   * 
   * @param xyz
   *          2.5 D coordinate
   * @return converted 2.5 D coordinate x,y alt stays the same
   * @throws ParserException
   *           in case of transformation error
   */
  public Xyz convertLine(Xyz xyz) throws ParserException {

    // Converted coordinates are rounded to 1 meter.
    Coordinate c = new Coordinate();
    double h;
    Point convP = null;
    Xyz convXyz = null;

    c.setOrdinate(Coordinate.X, xyz.getX());
    c.setOrdinate(Coordinate.Y, xyz.getY());
    h = xyz.getAlt();
    Point p = gf.createPoint(c);
    try {
      convP = (Point) JTS.transform(p, this.mathTransform);
    } catch (MismatchedDimensionException e) {
      throw new ParserException("Wrong dimension while converting point.");
    } catch (TransformException e) {
      throw new ParserException("Error while transforming between CRS.");
    }
    if (convP != null) {
      // TODO: Do real rounding, this works just because data is always under .5
      convXyz = new Xyz((int)convP.getCoordinate().getOrdinate(Coordinate.X),
          (int)convP.getCoordinate().getOrdinate(Coordinate.Y), h);
    }

    return convXyz;
  }

}
