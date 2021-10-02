package org.example.virusgame;

import org.example.virusgame.controllers.GameController;

public class VirusGame {
    public static void main(String[] args) {
        var gameController = new GameController();
        gameController.start();
    }
}
