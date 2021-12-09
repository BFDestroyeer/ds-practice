package edu.bfdestroyeer.virusgame.models;

import java.io.Serializable;

public class GameTurn implements Serializable {
    private int column = 0;
    private int row = 0;
    private boolean isSkipTurn = true;

    public GameTurn(Integer column, Integer row) {
        if (column != null && row != null)  {
            this.row = row;
            this.column = column;
            this.isSkipTurn = false;
        }
    }

    public int getColumn() {
        return this.column;
    }

    public int getRow() {
        return this.row;
    }

    public boolean isSkipTurn() {
        return this.isSkipTurn;
    }
}
