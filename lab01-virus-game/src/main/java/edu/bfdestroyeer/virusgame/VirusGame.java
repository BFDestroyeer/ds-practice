package edu.bfdestroyeer.virusgame;

import edu.bfdestroyeer.virusgame.controllers.GameController;

public class VirusGame {
    public static void main(String[] args) {
        var gameController = new GameController();
        gameController.start();
    }
}
