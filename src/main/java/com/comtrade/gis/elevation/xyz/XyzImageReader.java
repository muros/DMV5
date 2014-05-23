package com.comtrade.gis.elevation.xyz;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

/**
 * The Class XyzImageReader provides implementation of actual reading of XZY
 * DMV5 elevation data.
 * 
 * @author muros
 */
public class XyzImageReader extends ImageReader {

  /**
   * The stream containing image data.
   */
  private ImageInputStream stream = null;

  /**
   * The width of image.
   */
  private int width;

  /**
   * The height of image.
   */
  private int height;

  /**
   * Elevation data is read from xyz DMV5 file. Data is in D96 CRS.
   */
  private ElevationXyz elevData;

  public XyzImageReader(XyzImageReaderSpi originatingProvider) {
    super(originatingProvider);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#getNumImages(boolean)
   */
  @Override
  public int getNumImages(boolean allowSearch) throws IOException {
    // It is always one image only.
    return 1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#getWidth(int)
   */
  @Override
  public int getWidth(int imageIndex) throws IOException {
    checkIndex(imageIndex);

    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#getHeight(int)
   */
  @Override
  public int getHeight(int imageIndex) throws IOException {
    checkIndex(imageIndex);

    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#getImageTypes(int)
   */
  @Override
  public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
      throws IOException {
    // Image is only supported as grayscale image with one band
    // containig 16 bit signed value representing elevation in meters.
    checkIndex(imageIndex);

    ImageTypeSpecifier imageType = null;
    
    int datatype = DataBuffer.TYPE_USHORT;
      
    List<ImageTypeSpecifier> l = new ArrayList<ImageTypeSpecifier>();
    imageType = ImageTypeSpecifier.createGrayscale(16, datatype, false);
    l.add(imageType);

    return l.iterator();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#read(int, javax.imageio.ImageReadParam)
   */
  @Override
  public BufferedImage read(int imageIndex, ImageReadParam param)
      throws IOException {
    BufferedImage bi = null;
    
    
    readFullXyz();
    short[][] rasterData = elevData.getRasterData();
    // Get the specified detination image or create a new one
    BufferedImage dst = getDestination(param,
                                       getImageTypes(0),
                                       rasterData.length, rasterData[0].length);
   
    createImage(dst, rasterData, rasterData.length, rasterData[0].length);

    return dst;
  }

  /**
   * Creates buffered image from elevation data that was read from xyz file vith
   * DMV5 data.
   * @param dst destination image that is allready prepared to fill with data.
   * 
   * @param rasterData
   *          the raster data that was normalized
   */
  private void createImage(BufferedImage dst, short[][] rasterData, int width, int hight) {
    BufferedImage bi = null;
//    Graphics big = null;

//    bi = new BufferedImage(width, hight, BufferedImage.TYPE_USHORT_GRAY); //TYPE_INT_RGB ali TYPE_USHORT_GRAY
    int val[] = {0};
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < hight; y++) {
        val[0] = rasterData[x][y];
        dst.getRaster().setPixel(x, y, val);
      }
    }

//    return bi;
  }

  /**
   * Read whole XYZ file.
   * 
   * @throws IIOException
   */
  private void readFullXyz() throws IIOException {
    // Creates parser with D48 as native CRS and D96 as destination CRS.
    XyzParser xyzParser;
    String line;
    this.elevData = new ElevationXyz();
    this.elevData.setCrs("EPSG:3794");

    try {
      xyzParser = new XyzParser("EPSG:3787", "EPSG:3794");
    } catch (ParserException e) {
      throw new IIOException("Error creating CRS conversion.");
    }

    if (stream == null) {
      throw new IllegalStateException("No input stream");
    }
    try {
      while ((line = stream.readLine()) != null) {
        Xyz xyz;
        xyz = xyzParser.parseLine(line);
        xyz = xyzParser.convertLine(xyz);
        elevData.addData(xyz);
      }
    } catch (ParserException pe) {
      throw new IIOException("Error parsing xyz elevation data file.", pe);
    } catch (IOException ioe) {
      throw new IIOException("Error reading xyz elvation data file.", ioe);
    }
    elevData.calculateBounds();
    elevData.normalize();
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#getStreamMetadata()
   */
  @Override
  public IIOMetadata getStreamMetadata() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#getImageMetadata(int)
   */
  @Override
  public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.ImageReader#setInput(java.lang.Object, boolean)
   */
  @Override
  public void setInput(Object input, boolean isStreamable) {
    super.setInput(input, isStreamable);
    if (input == null) {
      this.stream = null;
      return;
    }
    if (input instanceof ImageInputStream) {
      this.stream = (ImageInputStream) input;
    } else {
      throw new IllegalArgumentException("bad input");
    }
  }

  /**
   * Check that there is always only one image in file, meaning that index is 0.
   * 
   * @param imageIndex
   *          the image index
   */
  private void checkIndex(int imageIndex) {
    if (imageIndex != 0) {
      throw new IndexOutOfBoundsException("bad index");
    }
  }

}
