/*
 *
 * Copyright (c) 2000-2007 by Rodney Kinney
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
package VASSAL.chat;

import VASSAL.build.module.Chatter;
import VASSAL.build.module.GlobalOptions;
import VASSAL.build.module.ServerConnection;
import VASSAL.tools.ProblemDialog;

/**
 * A window for private messages between the user and another player;
 */
public class PrivateChatter extends Chatter {
  private static final long serialVersionUID = 1L;

  private final ChatServerConnection client;
  private final Player other;

  public PrivateChatter(Player other, ChatServerConnection client) {
    this.other = other;
    this.client = client;
  }

  /** @deprecated Use {@link GlobalOptions#getInstance()#getPlayerId} */
  @Override
  @Deprecated(since = "2020-08-06", forRemoval = true)
  public String getHandle() {
    ProblemDialog.showDeprecated("2020-08-06");
    return GlobalOptions.getInstance().getPlayerId();
  }

  @Override
  public void send(String msg) {
    if (msg != null && !msg.isEmpty()) {
      show(msg);
      PrivMsgCommand c = new PrivMsgCommand(null, client.getUserInfo(), msg);
      client.sendTo(other, c);
    }
  }

  public Player getPlayer() {
    return other;
  }

  public ServerConnection getClient() {
    return client;
  }
}
