package org.appfuse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Static utility class to help with War file processing.
 */
public class WarPathUtils
{
  public static final Pattern WEBINF_CLASSES_PATTERN;

  static
  {
    WEBINF_CLASSES_PATTERN = Pattern.compile("^[/\\\\]?web-inf[/\\\\]classes[/\\\\]?(.*)$", Pattern.CASE_INSENSITIVE);
  }

  /**
   * Unpack the WEB-INF/classes directory from the specified war file if the contents of the target
   * directory is older than the war file.
   *
   * @param warFile        The war file to unpack.
   * @param classesJarFile The target jar file which will hold the contents of the WEB-INF/classes directory.
   * @throws IOException if an I/O error occurs.
   */
  public static void unpackWarClassesIfNewer(File warFile, File classesJarFile) throws IOException
  {
    boolean process = false;
    if (!classesJarFile.exists() || warFile.lastModified() > classesJarFile.lastModified())
    {
      process = true;
    }

    if (process)
    {
      ZipFile zipFile = new ZipFile(warFile);
      JarOutputStream classesJarOutputStream = new JarOutputStream(new FileOutputStream(classesJarFile));
      try
      {
        unpackWebInfClasses(zipFile, classesJarOutputStream);
      }
      finally
      {
        closeZipFile(zipFile);
        closeOutputStream(classesJarOutputStream);
      }
    }
  }

  private static void closeOutputStream(JarOutputStream classesJarOutputStream)
  {
    if (classesJarOutputStream != null)
    {
      try
      {
        classesJarOutputStream.close();
      }
      catch (IOException e)
      {
        // do nothing.
      }
    }
  }

  private static void closeZipFile(ZipFile zipFile)
  {
    if (zipFile != null)
    {
      try
      {
        zipFile.close();
      }
      catch (IOException e)
      {
        // do nothing.
      }
    }
  }

  /**
   * Extract the WEB-INF/classes entries from the specified war file into the named directory.
   *
   * @param zipFile                The war file to unpack
   * @param classesJarOutputStream The target directory.
   * @throws IOException if an I/O error occurs during the unpacking.
   */
  public static void unpackWebInfClasses(ZipFile zipFile, JarOutputStream classesJarOutputStream) throws IOException
  {
    Enumeration zipEntries = zipFile.entries();

    while (zipEntries.hasMoreElements())
    {
      ZipEntry zipEntry = (ZipEntry) zipEntries.nextElement();
      Matcher matcher = WEBINF_CLASSES_PATTERN.matcher(zipEntry.getName());
      if (matcher.matches() && matcher.group(1).length() > 0)
      {
        String classesEntry = matcher.group(1);

        ZipEntry jarEntry = new ZipEntry(classesEntry);

        jarEntry.setComment(zipEntry.getComment());
        jarEntry.setExtra(zipEntry.getExtra());
        jarEntry.setMethod(zipEntry.getMethod());
        jarEntry.setTime(zipEntry.getTime());
        jarEntry.setSize(zipEntry.getSize());
        jarEntry.setCompressedSize(zipEntry.getCompressedSize());
        jarEntry.setCrc(zipEntry.getCrc());
        classesJarOutputStream.putNextEntry(jarEntry);
        byte[] readBuffer = new byte[1024];
        int readLength;
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        while ((readLength = inputStream.read(readBuffer)) >= 0)
        {
          classesJarOutputStream.write(readBuffer, 0, readLength);
        }

      }
    }
  }

}
