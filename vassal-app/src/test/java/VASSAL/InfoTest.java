package VASSAL;

import org.junit.Test;

import VASSAL.tools.version.VersionUtils;

import static org.junit.Assert.*;

public class InfoTest {
  @Test
  public void testIsModuleTooNewYes() {
    assertTrue(Info.isModuleTooNew(VersionUtils.nextMinorVersion(Info.getVersion())));
  }

  @Test
  public void testIsModuleTooNewNo() {
    assertFalse(Info.isModuleTooNew(Info.getVersion()));
  }

  @Test
  public void testHasOldFormatYes() {
    assertTrue(Info.hasOldFormat("0.0.1"));
  }

  @Test
  public void testHasOldFormatNo() {
    assertFalse(Info.hasOldFormat(Info.getVersion()));
  }
}
