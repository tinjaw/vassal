/*
 *
 * Copyright (c) 2003 by Rodney Kinney
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package VASSAL;

import VASSAL.tools.ProblemDialog;
import VASSAL.tools.version.VassalVersionTokenizer;
import VASSAL.tools.version.VersionFormatException;
import VASSAL.tools.version.VersionTokenizer;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;

import VASSAL.tools.version.GitProperties;
import org.apache.commons.lang3.SystemUtils;

import VASSAL.tools.version.VersionUtils;

/**
 * Class for storing release-related information
 */
public final class Info {
  private static final GitProperties gitProperties;

  // Do not allow editing of modules with this revision or later
  private static final String EXPIRY_VERSION = "3.4";  //$NON-NLS-1$

  // Warn about editing modules, saves, logs written before this version
  private static final String UPDATE_VERSION =  "3.2";

  private static final File homeDir;
  private static final File tmpDir;

  // Directory setup
  static {
    File f;

    if (SystemUtils.IS_OS_MAC_OSX) {
      f = new File(
        System.getProperty("user.home"), "Library/Application Support/VASSAL"
      );
    }
    else if (SystemUtils.IS_OS_WINDOWS) {
      f = new File(System.getenv("APPDATA") + "/VASSAL");
    }
    else {
      f = new File(System.getProperty("user.home"), ".VASSAL");
    }

    homeDir = f;
    tmpDir = new File(homeDir, "tmp");

    if (!homeDir.exists()) {
      homeDir.mkdirs();
    }

    if (!tmpDir.exists()) {
      tmpDir.mkdirs();
    }

    // Set the instance id from the system properties.
    final String idstr = System.getProperty("VASSAL.id");
    if (idstr == null) {
      instanceID = 0;
    }
    else {
      int id;
      try {
        id = Integer.parseInt(idstr);
      }
      catch (NumberFormatException e) {
        id = -1;
      }

      instanceID = id;
    }

    gitProperties = new GitProperties();
  }

  /** The path to the JVM binary. */
  public static final String javaBinPath =
    System.getProperty("java.home") +
      File.separator +
      "bin" +
      File.separator +
      "java";

  /** This class should not be instantiated */
  private Info() { }

  /**
   * A valid version format is "w.x.y[-z]", where 'w','x', and 'y' are
   * integers and z is a string. In the version number, w.x are the major/minor release number,
   * y is the bug-fix release number, and the 'z' identifies an intermediate build
   * e.g. 3.3.3-allpha1 or 3.3.3-SNAPSHOT
   *
   * @return the full version of the VASSAL engine.
   */
  public static String getVersion() {
    return gitProperties.getVersion();
  }

  /**
   * @return The major/minor portion of the release version. If the version is a
   * beta-release number, a 'beta' is appended. For example, the minor
   * version of 3.0.2 is 3.0, and the minor version of 3.0b3 is 3.0beta.
   *
   * @deprecated If you need the minor version number, get it from
   * a {@link VersionTokenizer}.
   */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static String getMinorVersion() {
    ProblemDialog.showDeprecated("2020-08-06");
    final VersionTokenizer tok = new VassalVersionTokenizer(gitProperties.getVersion());
    try {
      return tok.next() + "." + tok.next();
    }
    catch (VersionFormatException e) {
      return null;
    }
  }

  /**
   * Bugzilla (and other potential external reporting tools) require onlt the primary numeric portion of
   * the version number. e.g. 3.3.3-SNAPSHOT return 3.3.3\
   *
   * @return The reportable version number
   */
  public static String getReportableVersion() {
    return getVersion().contains("-") ?  getVersion().substring(0, getVersion().indexOf('-')) : getVersion();
  }

  private static final int instanceID;

  /**
   * Returns the instance id for this process. The instance id will be
   * be unique across the Module Manager and its children.
   */
  public static int getInstanceID() {
    return instanceID;
  }

  /**
   * @return size of screen accounting for the screen insets (i.e. Windows taskbar)
   */
  public static Rectangle getScreenBounds(Component c) {
    final Rectangle bounds =
      new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    final GraphicsConfiguration config = c.getGraphicsConfiguration();
    final Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);
    bounds.translate(insets.left, insets.top);
    bounds.setSize(bounds.width - insets.left - insets.right,
                   bounds.height - insets.top - insets.bottom);
    return bounds;
  }

  /** @deprecated Use {@link SystemUtils#IS_OS_MAC_OSX} instead */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static boolean isMacOSX() {
    ProblemDialog.showDeprecated("2020-08-06");
    return SystemUtils.IS_OS_MAC_OSX;
  }

  /** @deprecated Use {@link SystemUtils#IS_OS_MAC_OSX} instead. */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static boolean isMacOsX() {
    ProblemDialog.showDeprecated("2020-08-06");
    return SystemUtils.IS_OS_MAC_OSX;
  }

  /** @deprecated Use {@link SystemUtils#IS_OS_WINDOWS} instead */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static boolean isWindows() {
    ProblemDialog.showDeprecated("2020-08-06");
    return SystemUtils.IS_OS_WINDOWS;
  }

  public static boolean isModuleTooNew(String version) {
    return VersionUtils.compareVersions(version, EXPIRY_VERSION) >= 0;
  }

  public static boolean hasOldFormat(String version) {
    return VersionUtils.compareVersions(version, UPDATE_VERSION) < 0;
  }

  /**
   * Compares VASSAL version strings. This method is guaranteed to
   * correctly compare the current version string with any other
   * version string. It is <em>not</em> guaranteed to correctly
   * compare two arbitrary version strings.
   *
   * @return negative if {@code v0 < v1}, positive if {@code v0 > v1}, and
   * zero if {@code v0 == v1} or if the ordering cannot be determined from
   * the parseable parts of the two <code>String</code>s.
   *
   * @deprecated use {@link VersionUtils#compareVersions(String, String)}
   */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static int compareVersions(String v0, String v1) {
    ProblemDialog.showDeprecated("2020-08-06");
    return VersionUtils.compareVersions(v0, v1);
  }

  /**
   * Returns the directory where VASSAL is installed.
   *
   * @return a {@link File} representing the directory
   */
  public static File getBaseDir() {
    return new File(System.getProperty("user.dir"));
  }

  /**
   * Returns the directory where the VASSAL documentation is installed.
   *
   * @return a {@link File} representing the directory
   * @deprecated Use {@link #getDocDir()} instead.
   */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static File getDocsDir() {
    ProblemDialog.showDeprecated("2020-08-06");
    return getDocDir();
  }

  public static File getBinDir() {
    return new File(System.getProperty("user.dir"));
  }

  public static File getDocDir() {
    final String d = SystemUtils.IS_OS_MAC_OSX ?
      "Contents/Resources/doc" : "doc";
    return new File(getBaseDir(), d);
  }

  public static File getConfDir() {
    return getHomeDir();
  }

  public static File getTempDir() {
    return tmpDir;
  }

  public static File getPrefsDir() {
    return new File(getConfDir(), "prefs");
  }

  // FIXME: this is a misleading name for this function
  public static File getHomeDir() {
    return homeDir;
  }

  /**
   * @return true if this platform supports Swing Drag and Drop
   * @deprecated Check is no longer necessary since Java 1.4+ is required.
   */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static boolean isDndEnabled() {
    ProblemDialog.showDeprecated("2020-08-06");
    return true;
  }

  /**
   * @deprecated since Java 1.4 is now required
   * @return true if this platform supports Java2D
   */
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public static boolean is2dEnabled() {
    ProblemDialog.showDeprecated("2020-08-06");
    return true;
  }
}
