package com.comtrade.gis.elevation.xyz;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

  /** The stream containing image data. */
  private ImageInputStream stream = null;

  /** The width of image. */
  private int width;

  /** The height of image. */
  private int height;

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
    int datatype = DataBuffer.TYPE_SHORT;
    List<ImageTypeSpecifier> l = new ArrayList<ImageTypeSpecifier>();
    imageType = ImageTypeSpecifier.createGrayscale(16, datatype, true);
    l.add(imageType);
    
    return l.iterator();
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
   * @see javax.imageio.ImageReader#read(int, javax.imageio.ImageReadParam)
   */
  @Override
  public BufferedImage read(int imageIndex, ImageReadParam param)
      throws IOException {
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
