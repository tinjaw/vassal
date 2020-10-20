/*
 * Copyright 2020 Vassal Development Team
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

package VASSAL.counters;

import VASSAL.tools.NamedKeyStroke;
import java.lang.reflect.InvocationTargetException;
import javax.swing.KeyStroke;
import org.junit.Test;

public class CloneTest extends DecoratorTest {


  @Test
  public void serializeTests() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

    // Default piece
    Clone trait = new Clone();
    serializeTest("Default trait", trait); // NON-NLS

    // Set a command name and NamedKeyStroke
    trait = new Clone();
    trait.commandName = "testCommand"; // NON-NLS
    trait.key = new NamedKeyStroke("xyzzy"); // NON-NLS
    serializeTest("NamedKeyStroke", trait); // NON-NLS

    // Set a command name and standard KeyStroke
    trait = new Clone();
    trait.commandName = "testCommand"; // NON-NLS
    trait.key = new NamedKeyStroke(KeyStroke.getKeyStroke(65, 0));
    serializeTest("KeyStroke", trait); // NON-NLS
  }
}
