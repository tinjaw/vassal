package VASSAL.chat;

import java.awt.Frame;
import java.awt.TextField;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import VASSAL.tools.io.IOUtils;

public class CompressorTest {

  @Ignore
  @Test
  public void runCompressor() throws Exception {
    compressorMain(new String[0]);
  }

  public static void compressorMain(String[] args) throws Exception {
    if (args.length == 0) {
      final Frame f = new Frame();
      final TextField tf = new TextField(60);
      f.add(tf);
      f.pack();
      f.setVisible(true);
      tf.addActionListener(evt -> {
        try {
          final String s = evt.getActionCommand();
          System.err.println("Input (" + s.length() + ") = " + s); //$NON-NLS-1$ //$NON-NLS-2$
          final String comp = new String(Compressor.compress(s.getBytes()));
          System.err.println("Compressed (" + comp.length() + ") = " + comp); //$NON-NLS-1$ //$NON-NLS-2$
          final String decomp = new String(Compressor.decompress(comp.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
          System.err.println("Decompressed (" + decomp.length() + ") = " + decomp); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // FIXME: review error message
        catch (IOException ex) {
          ex.printStackTrace();
        }
      });
    }
    else {
      final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      final FileInputStream file = new FileInputStream(args[0]);
      try {
        IOUtils.copy(file, byteOut);
      }
      finally {
        try {
          file.close();
        }
        // FIXME: review error message
        catch (IOException e) {
          e.printStackTrace();
        }
      }

      final byte[] contents = byteOut.toByteArray();
      if (contents[0] == 'P' && contents[1] == 'K') {
        final byte[] uncompressed = Compressor.decompress(contents);
        final FileOutputStream out =
          new FileOutputStream(args[0] + ".uncompressed"); //$NON-NLS-1$
        try {
          out.write(uncompressed);
        }
        finally {
          try {
            out.close();
          }
          // FIXME: review error message
          catch (IOException e) {
            e.printStackTrace();
          }
        }

        final byte[] recompressed = Compressor.compress(uncompressed);
        if (!Arrays.equals(recompressed, contents)) {
// FIXME: don't throw unchecked exception
          throw new RuntimeException("Compression failed"); //$NON-NLS-1$
        }
      }
      else {
        final byte[] compressed = Compressor.compress(contents);
        final FileOutputStream out =
          new FileOutputStream(args[0] + ".compressed"); //$NON-NLS-1$
        try {
          out.write(compressed);
        }
        finally {
          try {
            out.close();
          }
          // FIXME: review error message
          catch (IOException e) {
            e.printStackTrace();
          }
        }

        if (!Arrays.equals(Compressor.decompress(compressed), contents)) {
// FIXME: don't throw unchecked exception
          throw new RuntimeException("Compression failed"); //$NON-NLS-1$
        }
      }
    }
  }

}
