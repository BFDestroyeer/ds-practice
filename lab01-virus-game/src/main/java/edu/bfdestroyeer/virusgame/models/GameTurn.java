package edu.bfdestroyeer.virusgame.models;

public class GameTurn {
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

    public GameTurn(String string) {
        var stringParts = string.split(";");
        this.row = Integer.parseInt(stringParts[0]);
        this.column = Integer.parseInt(stringParts[1]);
        this.isSkipTurn = Boolean.parseBoolean(stringParts[2]);
    }

    @Override
    public String toString() {
        return this.row + ";" + this.column + ";" + Boolean.toString(this.isSkipTurn);
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
