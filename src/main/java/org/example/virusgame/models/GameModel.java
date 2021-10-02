package org.example.virusgame.models;

import java.util.Arrays;

public class GameModel {
    CellStatus[][] gameField = new CellStatus[10][10];

    public GameModel() {
        for (CellStatus[] cellStatuses : gameField) {
            Arrays.fill(cellStatuses, CellStatus.EMPTY);
        }
    }

    public void makeTurn(int row, int line, GameRole gameRole) throws IllegalArgumentException{
        if (gameRole == GameRole.SERVER) {
            gameField[row][line] = CellStatus.SERVER_ALIVE;
        } else if (gameRole == GameRole.CLIENT) {
            gameField[row][line] = CellStatus.CLIENT_ALIVE;
        }
    }

    public CellStatus[][] getGameField() {
        return  gameField;
    }
}
