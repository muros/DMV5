package com.comtrade.gis.elevation.xyz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;

/**
 * The Class XyzImageReader provides implementation of
 * actual reading of XZY DMV5 elevation data.
 * 
 * @author muros
 */
public class XyzImageReader extends ImageReader {

  public XyzImageReader(XyzImageReaderSpi originatingProvider) {
    super(originatingProvider);
  }

  @Override
  public int getNumImages(boolean allowSearch) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getWidth(int imageIndex) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getHeight(int imageIndex) throws IOException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
      throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IIOMetadata getStreamMetadata() throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public BufferedImage read(int imageIndex, ImageReadParam param)
      throws IOException {
    // TODO Auto-generated method stub
    return null;
  }

}
