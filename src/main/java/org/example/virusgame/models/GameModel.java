package org.example.virusgame.models;

import java.util.Arrays;

public class GameModel {
    public final static int FIELD_LENGTH = 10;

    CellStatus[][] gameField = new CellStatus[FIELD_LENGTH][FIELD_LENGTH];

    public GameModel() {
        for (CellStatus[] cellStatuses : gameField) {
            Arrays.fill(cellStatuses, CellStatus.EMPTY);
        }
    }

    public void makeTurn(int column, int row, GameRole gameRole) throws IllegalArgumentException {
        if ((column < 0) || (column > (FIELD_LENGTH - 1)) || (row < 0) || (row > (FIELD_LENGTH - 1))) {
            throw new IllegalArgumentException("Illegal turn");
        }
        if (!isCellAvailable(column, row, gameRole)) {
            throw new IllegalArgumentException("Illegal turn");
        }
        if (gameRole == GameRole.SERVER) {
            if (gameField[column][row] == CellStatus.EMPTY) {
                this.gameField[column][row] = CellStatus.SERVER_ALIVE;
            } else if (gameField[column][row] == CellStatus.CLIENT_ALIVE) {
                this.gameField[column][row] = CellStatus.CLIENT_KILLED;
            }
        } else if (gameRole == GameRole.CLIENT) {
            if (gameField[column][row] == CellStatus.EMPTY) {
                this.gameField[column][row] = CellStatus.CLIENT_ALIVE;
            } else if (gameField[column][row] == CellStatus.SERVER_ALIVE) {
                this.gameField[column][row] = CellStatus.SERVER_KILLED;
            }
        }
    }

    private boolean isCellAvailable(int column, int row, GameRole gameRole) {
        if (gameRole == GameRole.SERVER
                && this.gameField[column][row] != CellStatus.EMPTY
                && this.gameField[column][row] != CellStatus.CLIENT_ALIVE) {
            return false;
        }
        if (gameRole == GameRole.CLIENT
                && this.gameField[column][row] != CellStatus.EMPTY
                && this.gameField[column][row] != CellStatus.SERVER_ALIVE) {
            return false;
        }

        if (column == 0 && row == 0
                && gameRole == GameRole.SERVER
                && this.gameField[column][row] == CellStatus.EMPTY) {
            return true;
        } else if ((column != 0 || row != 0)
                && gameRole == GameRole.SERVER
                && this.gameField[0][0] == CellStatus.EMPTY) {
            return false;
        }

        if (column == (FIELD_LENGTH - 1) && row == (FIELD_LENGTH - 1)
                && gameRole == GameRole.CLIENT
                && this.gameField[column][row] == CellStatus.EMPTY) {
            return true;
        } else if ((column != (FIELD_LENGTH - 1) || row != (FIELD_LENGTH - 1))
                && gameRole == GameRole.CLIENT
                && this.gameField[(FIELD_LENGTH - 1)][(FIELD_LENGTH - 1)] == CellStatus.EMPTY) {
            return false;
        }

        for (var i = Math.max(0, column - 1); i < Math.min(FIELD_LENGTH, column + 2); i++) {
            for (var j = Math.max(0, row - 1); j < Math.min(FIELD_LENGTH, row + 2); j++) {
                if (gameRole == GameRole.SERVER && ((this.gameField[i][j] == CellStatus.SERVER_ALIVE)
                        || (this.gameField[i][j] == CellStatus.CLIENT_KILLED))) {
                    return true;
                } else if (gameRole == GameRole.CLIENT && ((this.gameField[i][j] == CellStatus.CLIENT_ALIVE)
                        || (this.gameField[i][j] == CellStatus.SERVER_KILLED))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPlayerVictory(GameRole gameRole) {
        for (var line : gameField) {
            for (var cell : line) {
                if (gameRole == GameRole.SERVER && cell == CellStatus.CLIENT_ALIVE) {
                    return false;
                } else if (gameRole == GameRole.CLIENT && cell == CellStatus.SERVER_ALIVE) {
                    return false;
                }
            }
        }
        return true;
    }

    public CellStatus[][] getGameField() {
        return  gameField;
    }
}
