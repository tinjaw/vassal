package VASSAL.build.module.turn;

import VASSAL.build.AbstractConfigurable;
import VASSAL.build.AutoConfigurable;
import VASSAL.build.Buildable;
import VASSAL.build.GameModule;
import VASSAL.build.module.Chatter;
import VASSAL.build.module.documentation.HelpFile;
import VASSAL.command.Command;
import VASSAL.configure.Configurer;
import VASSAL.configure.NamedHotKeyConfigurer;
import VASSAL.configure.PlayerIdFormattedStringConfigurer;
import VASSAL.configure.PropertyExpression;
import VASSAL.counters.BasicPiece;
import VASSAL.i18n.Resources;
import VASSAL.i18n.TranslatableConfigurerFactory;
import VASSAL.tools.FormattedString;
import VASSAL.tools.NamedKeyStroke;

import java.util.Collections;
import java.util.List;

public class TurnGlobalHotkey extends AbstractConfigurable {

  public static final String NAME = "name"; //$NON-NLS-1$
  public static final String HOTKEY = "hotkey"; //$NON-NLS-1$
  public static final String MATCH = "match"; //$NON-NLS-1$
  public static final String REPORT_FORMAT = "reportFormat"; //$NON-NLS-1$

  protected PropertyExpression match = new PropertyExpression();
  protected NamedKeyStroke hotkey;
  protected FormattedString format = new FormattedString();
  protected BasicPiece checkPiece = new BasicPiece();

  @Override
  public String[] getAttributeDescriptions() {
    return new String[] {
      Resources.getString("Editor.description_label"),
      Resources.getString("Editor.TurnGlobalHotkey.global_hotkey"),
      Resources.getString("Editor.TurnGlobalHotkey.match_properties"),
      Resources.getString("Editor.report_format")
    };
  }

  @Override
  public Class<?>[] getAttributeTypes() {
    return new Class<?>[] {
      String.class,
      NamedKeyStroke.class,
      PropertyExpression.class,
      ReportFormatConfig.class
    };
  }

  public static class ReportFormatConfig implements TranslatableConfigurerFactory {
    @Override
    public Configurer getConfigurer(AutoConfigurable c, String key, String name) {
      return new PlayerIdFormattedStringConfigurer(key, name, new String[]{});
    }
  }

  @Override
  public String[] getAttributeNames() {
    return new String[] {NAME, HOTKEY, MATCH, REPORT_FORMAT};
  }

  @Override
  public String getAttributeValueString(String key) {
    if (NAME.equals(key)) {
      return getConfigureName();
    }
    else if (HOTKEY.equals(key)) {
      return NamedHotKeyConfigurer.encode(hotkey);
    }
    else if (MATCH.equals(key)) {
      return match.getExpression();
    }
    else if (REPORT_FORMAT.equals(key)) {
      return format.getFormat();
    }
    else
      return null;
  }

  @Override
  public void setAttribute(String key, Object value) {
    if (NAME.equals(key)) {
      setConfigureName((String) value);
    }
    else if (HOTKEY.equals(key)) {
      if (value instanceof String) {
        value = NamedHotKeyConfigurer.decode((String) value);
      }
      hotkey = (NamedKeyStroke) value;
    }
    else if (MATCH.equals(key)) {
      match.setExpression((String) value);
    }
    else if (REPORT_FORMAT.equals(key)) {
      format.setFormat((String) value);
    }

  }

  @Override
  public Class<?>[] getAllowableConfigureComponents() {
    return new Class<?>[0];
  }

  @Override
  public void removeFrom(Buildable parent) {
  }

  @Override
  public void addTo(Buildable parent) {
  }

  public static String getConfigureTypeName() {
    return Resources.getString("Editor.TurnGlobalHotkey.component_type");
  }

  @Override
  public HelpFile getHelpFile() {
    return HelpFile.getReferenceManualPage("TurnTracker.html", "Hotkey"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public void apply() {
    if (match.isNull() || match.accept(checkPiece)) {
      GameModule.getGameModule().fireKeyStroke(hotkey);
      String reportText = format.getLocalizedText();
      if (reportText.length() > 0) {
        Command c = new Chatter.DisplayText(GameModule.getGameModule().getChatter(), "* " + reportText);
        c.execute();
        GameModule.getGameModule().sendAndLog(c);
      }
    }
  }


  /**
   * {@link VASSAL.search.SearchTarget}
   * @return a list of the Configurable's string/expression fields if any (for search)
   */
  @Override
  public List<String> getExpressionList() {
    return List.of(match.getExpression());
  }

  /**
   * {@link VASSAL.search.SearchTarget}
   * @return a list of any Message Format strings referenced in the Configurable, if any (for search)
   */
  @Override
  public List<String> getFormattedStringList() {
    return List.of(format.getFormat());
  }

  /**
   * {@link VASSAL.search.SearchTarget}
   * @return a list of any Property Names referenced in the Configurable, if any (for search)
   */
  @Override
  public List<String> getPropertyList() {
    return Collections.emptyList();
  }

  /**
   * {@link VASSAL.search.SearchTarget}
   * @return a list of any Named KeyStrokes referenced in the Configurable, if any (for search)
   */
  @Override
  public List<NamedKeyStroke> getNamedKeyStrokeList() {
    return Collections.singletonList(NamedHotKeyConfigurer.decode(getAttributeValueString(HOTKEY)));
  }
}
