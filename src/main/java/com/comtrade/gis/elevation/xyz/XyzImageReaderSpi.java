package com.comtrade.gis.elevation.xyz;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * The Class XyzImageReaderSpi provides ImageIO reader for Digital Elevation
 * Model (DEM) data. Data is provided by Geodetska Uprava Republike Slovenije.
 * 
 * See <a href="http://www.e-prostor.gov.si/
 * zbirke_prostorskih_podatkov/topografski_in_kartografski_podatki/
 * digitalni_model_visin/digitalni_model_visin_5_x_5_m_dmv_5/"> DMV5</a>
 * 
 * @author muros
 */
public class XyzImageReaderSpi extends ImageReaderSpi {

  static final String vendorName = "Comtrade d.o.o.";
  static final String version = "dev";
  static final String readerClassName = "com.comtrade.gis.elevation.xyz.XyzImageReader";
  static final Class[] inputTypes = { ImageInputStream.class };
  static final String[] names = { "DMV5" };
  static final String[] suffixes = { "xyz" };
  static final String[] MIMETypes = { "image/x-dmv5" };
  static final String[] writerSpiNames = {};

  // Metadata formats, more information below
  static final boolean supportsStandardStreamMetadataFormat = false;
  static final String nativeStreamMetadataFormatName = null;
  static final String nativeStreamMetadataFormatClassName = null;
  static final String[] extraStreamMetadataFormatNames = null;
  static final String[] extraStreamMetadataFormatClassNames = null;
  static final boolean supportsStandardImageMetadataFormat = false;
  static final String nativeImageMetadataFormatName = "com.mycompany.imageio.MyFormatMetadata_1.0";
  static final String nativeImageMetadataFormatClassName = "com.mycompany.imageio.MyFormatMetadata";
  static final String[] extraImageMetadataFormatNames = null;
  static final String[] extraImageMetadataFormatClassNames = null;

  public XyzImageReaderSpi() {
    super(vendorName, version, names, suffixes, MIMETypes, readerClassName,
        inputTypes, writerSpiNames, supportsStandardStreamMetadataFormat,
        nativeStreamMetadataFormatName, nativeStreamMetadataFormatClassName,
        extraStreamMetadataFormatNames, extraStreamMetadataFormatClassNames,
        supportsStandardImageMetadataFormat, nativeImageMetadataFormatName,
        nativeImageMetadataFormatClassName, extraImageMetadataFormatNames,
        extraImageMetadataFormatClassNames);
  }

  /* (non-Javadoc)
   * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
   */
  public String getDescription(Locale locale) {
    return "Reader for XZY DMV5 elevation data.";
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.imageio.spi.ImageReaderSpi#canDecodeInput(java.lang.Object)
   */
  public boolean canDecodeInput(Object input) throws IOException {
    // Check that first line contains three space separated double values.
    if (!(input instanceof ImageInputStream)) {
      return false;
    }

    ImageInputStream stream = (ImageInputStream) input;
    String firstLine;
    try {
      stream.mark();
      firstLine = stream.readLine();
      stream.reset();
    } catch (IOException e) {
      return false;
    }

    if (firstLine != null) {
      String[] parts = firstLine.split("\\s");
      if (parts != null) {
        if (parts.length == 3) {
          try {
            Double.valueOf(parts[0]);
            Double.valueOf(parts[1]);
            Double.valueOf(parts[2]);
          } catch (NumberFormatException nfe) {
            return false;
          }
        } else {
          return false;
        }
      }
    }

    return true;
  }

  public ImageReader createReaderInstance(Object extension) {
    return new XyzImageReader(this);
  }

}
