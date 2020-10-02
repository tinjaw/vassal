/*
 * Copyright (c) 2020 by the Vassal developers
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
package VASSAL.configure.password;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import VASSAL.i18n.Resources;
import VASSAL.tools.icon.IconFactory;
import VASSAL.tools.icon.IconFamily;

public class ToggleablePasswordFieldPanel {
  private static final char ECHO_CHAR = '•';

  private final JPanel panel;
  private final JPasswordField passwordField;

  private boolean passwordVisible = false;

  public ToggleablePasswordFieldPanel(String label, String initialPassword) {
    panel = new JPanel(new MigLayout("ins 0", "[]rel[][]")); //NON-NLS

    final JLabel passwordLabel = new JLabel(label);
    panel.add(passwordLabel);

    passwordField = new JPasswordField(12);
    passwordField.setEchoChar(ECHO_CHAR);
    passwordField.setText(initialPassword);
    passwordLabel.setLabelFor(passwordField);

    panel.add(passwordField, "pushx, growx, sizegroupy foo"); //NON-NLS

    final Icon eye = IconFactory.getIcon("eye", IconFamily.XSMALL); //NON-NLS
    final Icon noEye = IconFactory.getIcon("eye-slash", IconFamily.XSMALL); //NON-NLS

    final JButton toggleButton = new JButton(noEye);
    toggleButton.setToolTipText(Resources.getString(
      "GlobalOptions.toggle_password_visibility" //$NON-NLS-1$
    ));
    toggleButton.addActionListener(e -> {
      passwordVisible = !passwordVisible;
      passwordField.setEchoChar(passwordVisible ? (char) 0 : ECHO_CHAR);
      toggleButton.setIcon(passwordVisible ? eye : noEye);
    });
    panel.add(toggleButton, "sizegroupy foo"); //NON-NLS
  }

  public JPanel getPanel() {
    return panel;
  }

  public String getPassword() {
    return String.valueOf(passwordField.getPassword());
  }

  public void setPassword(String password) {
    passwordField.setText(password);
  }

  public void addPasswordFieldListener(DocumentListener documentListener) {
    passwordField.getDocument().addDocumentListener(documentListener);
  }
}
