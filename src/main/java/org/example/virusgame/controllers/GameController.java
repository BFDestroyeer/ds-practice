package org.example.virusgame.controllers;

import org.example.virusgame.models.GameModel;
import org.example.virusgame.models.GameRole;
import org.example.virusgame.views.GameView;

public class GameController {

    private final GameModel gameModel = new GameModel();
    private final GameView gameView = new GameView();

    private GameRole gameRole;
    private String serverIp;

    public void start() {
        this.gameRole = gameView.getGameRole();
        this.serverIp = gameView.getServerIp();
    }
}
