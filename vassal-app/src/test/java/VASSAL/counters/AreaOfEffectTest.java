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
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import javax.swing.KeyStroke;
import org.junit.Test;

public class AreaOfEffectTest extends DecoratorTest {


  @Test
  public void serializeTests() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

    // Default piece
    AreaOfEffect trait = new AreaOfEffect();
    serializeTest("Default trait", trait); // NON-NLS

    // Always active, no map shader, fixed radius
    trait = new AreaOfEffect();
    trait.transparencyColor = Color.CYAN;
    trait.transparencyLevel = 42;
    trait.radius = 3;
    trait.alwaysActive = true;
    trait.fixedRadius = true;
    serializeTest("Always active, no map shader, fixed radius", trait); // NON-NLS

    // Always active, map shader, variable radius
    trait = new AreaOfEffect();
    trait.transparencyColor = Color.CYAN;
    trait.transparencyLevel = 42;
    trait.mapShaderName = "plugh"; // NON-NLS
    trait.radius = 3;
    trait.alwaysActive = true;
    trait.fixedRadius = false;
    trait.radiusMarker = "xyzzy"; // NON-NLS
    serializeTest("Always active, map shader, variable radius", trait); // NON-NLS

    // NameKeyStroke, no map shader, fixed radius
    trait = new AreaOfEffect();
    trait.transparencyColor = Color.CYAN;
    trait.transparencyLevel = 42;
    trait.radius = 3;
    trait.alwaysActive = false;
    trait.activateCommand = "Activate"; // NON-NLS
    trait.activateKey = new NamedKeyStroke("xyzzy"); // NON-NLS
    trait.fixedRadius = true;
    serializeTest("NamedKeystroke, no map shader, fixed radius", trait); // NON-NLS

    // KeyStroke, no map shader, fixed radius
    trait = new AreaOfEffect();
    trait.transparencyColor = Color.CYAN;
    trait.transparencyLevel = 42;
    trait.radius = 3;
    trait.alwaysActive = false;
    trait.activateCommand = "Activate"; // NON-NLS
    trait.activateKey = new NamedKeyStroke(KeyStroke.getKeyStroke(65, 0));
    trait.fixedRadius = true;
    serializeTest("KeyStroke, no map shader, fixed radius", trait); // NON-NLS
  }
}
