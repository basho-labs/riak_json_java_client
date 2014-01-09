package com.basho.riak.json.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Junk Drawer of Things w/ Streams
 * 
 * @author Randy Secrist
 */
public class StreamUtils {
  public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[(1 << 10) * 8];
    int count = 0;
    do {
      out.write(buffer, 0, count);
      out.flush();
      count = in.read(buffer, 0, buffer.length);
    }
    while (count != -1);
  }
  
  public static final byte[] rewindStream(InputStream stream) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    if (stream.markSupported()) {
      try {
        stream.reset();
        copyInputStream(stream, bos);
      }
      catch(Throwable t) { /* suppressed */ ; }
    }
    return bos.toByteArray();
  }
}
