package com.comtrade.geo.converter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * 
 */
public class App extends JFrame {
  
  private static int ycnt;
  
  private static int xcnt;
  
  private static int lastX;

  private static int lastY;

  private static double lastH;
  
  private static int minX = Integer.MAX_VALUE;
  
  private static int maxX;
  
  private static int minY = Integer.MAX_VALUE;
  
  private static int maxY;
  
  private static double minH = 10000;
  
  private static double maxH;

  private String[] m_args;
  
  //create a component that you can actually draw on.
  class DrawPane extends JPanel {
    public void paintComponent(Graphics g){
      //draw on g here e.g.
      //g.fillRect(20, 20, 100, 200);
       try {
        go(m_args, g);
      } catch (MismatchedDimensionException | TransformException | FactoryException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } 
     }
  }
  
  public App(String[] args) {
    super("XYZ");
    
    m_args = args;

    setContentPane(new DrawPane());

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setSize(800, 800);

    setVisible(true); 
}

  public static void main(String[] args) {
    new App(args);
  }
  
  private void go(String[] args, Graphics g) throws NoSuchAuthorityCodeException,
      FactoryException, MismatchedDimensionException, TransformException {
    GeometryFactory gf = new GeometryFactory();
    // Coordinate c = new Coordinate(382572.28, 97732.29);

    FileInputStream fis;
    BufferedReader br = null; 
    try {
//      fis = new FileInputStream("VTC2641.xyz");
      fis = new FileInputStream("VTC2641.xyz");
      br = new BufferedReader(new InputStreamReader(fis,
          Charset.forName("UTF-8")));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    String line;
//    CoordinateReferenceSystem d48Crs = CRS.decode("EPSG:3912");
    CoordinateReferenceSystem d48Crs = CRS.decode("EPSG:3787");
    CoordinateReferenceSystem googleCrs = CRS.decode("EPSG:3857");
    CoordinateReferenceSystem d96Crs = CRS.decode("EPSG:3794");
    MathTransform mathTransform = CRS.findMathTransform(d48Crs,
        d96Crs, true);
//    DefaultGeographicCRS.WGS84, false);
//        googleCrs, false);

    BufferedImage bi = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
    Graphics big = bi.getGraphics();
    try {
      while ((line = br.readLine()) != null) {
        String[] linParts = line.split(" ");

        Coordinate c = new Coordinate();
        double h;
        c.setOrdinate(Coordinate.X, Double.parseDouble(linParts[0]));
        c.setOrdinate(Coordinate.Y, Double.parseDouble(linParts[1]));
        h = Double.parseDouble(linParts[2]);

        Point p = gf.createPoint(c);
        Point p1 = (Point) JTS.transform(p, mathTransform);

        // Izpis vsega originalnih ini pretvorjenih koordinat in višine
//        System.out.println("LON / LAT / HGT "
//            + getDeg(p1.getCoordinate().getOrdinate(Coordinate.X)) + " / "
//            + getDeg(p1.getCoordinate().getOrdinate(Coordinate.Y)) + " / "
//            + linParts[2] + " m " + "original: " + p + " -> " + 
//            (int)p1.getCoordinate().getOrdinate(Coordinate.X) + " " +
//            (int)p1.getCoordinate().getOrdinate(Coordinate.Y));
        
        // Izpis koordinat pretvorjenih iz D48 nazaj v D96 v katerih so bili podatki
        // zajeti in bi morale predstavljati pravilno mrežo.
        int x = (int)p1.getCoordinate().getOrdinate(Coordinate.X);
        int y = (int)p1.getCoordinate().getOrdinate(Coordinate.Y);
        evalBounds(x, y, h);
//        System.out.println( p1.getCoordinate().getOrdinate(Coordinate.X) + " " +
//            p1.getCoordinate().getOrdinate(Coordinate.Y));
//        System.out.println( x + " " + y);
        // Za C2601
        System.out.println((x - 409630)/5 + " " + (y - 130490)/5);
        int val = (int)((h - 966)/4.4);
        Color color = new Color(val, val, val);
        big.setColor(color);
        big.drawLine((x - 409630)/5, (y - 130490)/5, (x - 409630)/5, (y - 130490)/5);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    g.drawImage(bi, 0, 0, null);

    // VTC2641.xyz
    //    409630, 130490 / 411875, 133485
    //    Hight bounds: 966.8 / 2088.5
    //    2088 - 966 = 1122
    //    1122 / 255 = 4.4
    //     
    System.out.println("Bounds:\n" +
        minX + ", " + minY + " / " +
        maxX + ", " + maxY);
    System.out.println("Hight bounds: " + minH + " / " + maxH);
  }

  private static void evalBounds(int x, int y, double h) {
      if (lastX != x) {
//        System.out.println("X changed to " + x);
      }
      
      if (lastY != y) {
//        System.out.println(lastX + " " + lastY);
//        System.out.println(x + " " + y);
//        System.out.println(ycnt);
        ycnt = 0;
      } else {
        ycnt++;
      }
      if (x > maxX) {
        maxX = x;
      }
      if (y > maxY) {
        maxY = y;
      }
      if (h > maxH) {
        maxH = h;
      }
      if (x < minX) {
        minX = x;
      }
      if (y < minY) {
        minY = y;
      }
      if (h < minH) {
        minH = h;
      }
      lastX = x;
      lastY = y;
      lastH = h;
  }

  private static String getDeg(double decimal) {

    StringBuilder degStr = new StringBuilder();

    int degrees = (int) (decimal);
    int minutes = (int) (60 * (decimal - degrees));
    double seconds = 3600d * (decimal - degrees - (minutes / 60d));
    int sec = (int)(seconds * 100);
    seconds = sec / 100d;

    degStr.append(degrees).append("\u00B0 ");
    degStr.append(minutes).append("\u2032 ");
    degStr.append(seconds).append("\u2033 ");

    return degStr.toString();
  }

}
