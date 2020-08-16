/*
 *
 * Copyright (c) 2000-2003 by Rodney Kinney
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

import VASSAL.tools.ProblemDialog;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * An iterator for GamePieces.  Takes an optional PieceFilter to
 * extract GamePiece instances from an Enumeration or Iterator.
 */
public class PieceIterator {
  private final Iterator<? extends GamePiece> pi;
  private PieceFilter filter;

  public PieceIterator(final Iterator<? extends GamePiece> i) {
    pi = i;
  }

  @Deprecated(since="2020-08-06", forRemoval = true)
  public <T extends GamePiece> PieceIterator(Enumeration<T> e) {
    this(e.asIterator());
    ProblemDialog.showDeprecated("2020-08-06");
  }

  public PieceIterator(final Iterator<? extends GamePiece> i, PieceFilter f) {
    filter = f;
    pi = new Iterator<>() {
      private GamePiece next;

      @Override
      public boolean hasNext() {
        if (next != null) return true;

        while (i.hasNext()) {
          next = i.next();
          if (filter == null || filter.accept(next)) return true;
        }

        next = null;
        return false;
      }

      @Override
      public GamePiece next() {
        if (next != null) {
          final GamePiece ret = next;
          next = null;
          return ret;
        }

        for (; ; next = i.next()) {
          if (filter == null || filter.accept(next)) {
            final GamePiece ret = next;
            next = null;
            return ret;
          }
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Deprecated
  public <T extends GamePiece> PieceIterator(Enumeration<T> e, PieceFilter f) {
    this(e.asIterator(), f);
  }

  public GamePiece nextPiece() {
    return pi.hasNext() ? pi.next() : null;
  }

  public boolean hasMoreElements() {
    return pi.hasNext();
  }

  public static final Predicate<GamePiece> VISIBLE =
    gamePiece -> !Boolean.TRUE.equals(gamePiece.getProperty(Properties.INVISIBLE_TO_ME));

  public static <T extends GamePiece> PieceIterator visible(Iterator<T> i) {
    return new PieceIterator(i, VISIBLE::test);
  }

  @Deprecated
  public static <T extends GamePiece> PieceIterator visible(Enumeration<T> e) {
    return PieceIterator.visible(e.asIterator());
  }
}
