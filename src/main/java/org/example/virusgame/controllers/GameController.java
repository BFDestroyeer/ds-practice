package org.example.virusgame.controllers;

import org.example.virusgame.models.GameModel;
import org.example.virusgame.models.GameRole;
import org.example.virusgame.models.GameTurn;
import org.example.virusgame.views.GameView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GameController {

    private final GameModel gameModel;
    private final GameView gameView ;

    private GameRole gameRole;
    private GameRole oppositeGameRole;

    private Socket socket;
    private InetAddress serverAddress;
    private final int serverPort = 5020;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private boolean lastTurnIsSkip = false;
    private boolean lastOppositeTurnIsSkip = false;

    private boolean victoryCheckEnabled = false;
    private boolean isWin = false;
    private boolean isLose = false;

    public GameController() {
        this.gameModel = new GameModel();
        this.gameView = new GameView(this.gameModel);
    }

    public void start() {
        this.gameRole = gameView.requestGameRole();
        if (gameRole == GameRole.SERVER) {
            this.oppositeGameRole = GameRole.CLIENT;
            startServer();
        } else if (gameRole == GameRole.CLIENT) {
            this.oppositeGameRole = GameRole.SERVER;
            this.serverAddress = gameView.requestServerIp();
            connectToServer();
        }

        try {
            gameView.updateGameField();
            if (this.gameRole == GameRole.CLIENT) {
                this.readTurns();
                victoryCheckEnabled = true;
            }
            while (true) {
                this.makeTurns();
                if (this.lastTurnIsSkip && this.lastOppositeTurnIsSkip) {
                    this.gameView.showMessage("DRAW");
                    return;
                }
                if (isWin && victoryCheckEnabled) {
                    this.gameView.showMessage("You WIN");
                    return;
                }
                victoryCheckEnabled = true;

                this.gameView.showMessage("Wait for turns...");
                readTurns();
                if (this.lastTurnIsSkip && this.lastOppositeTurnIsSkip) {
                    this.gameView.showMessage("DRAW");
                    return;
                }
                if (this.isLose) {
                    this.gameView.showMessage("You LOSE");
                    return;
                }
                gameView.updateGameField();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void makeTurns() throws IOException {
        for (var i = 0; i < 3; i++) {
            GameTurn gameTurn;
            if (i == 0) {
                gameTurn = gameView.makeTurn(this.gameRole, true);
            } else {
                gameTurn = gameView.makeTurn(this.gameRole, false);
            }

            dataOutputStream.writeUTF(gameTurn.toString());
            if (!gameTurn.isSkipTurn()) {
                gameView.updateGameField();
                this.lastTurnIsSkip = false;
                this.isWin = this.gameModel.isPlayerVictory(this.gameRole);
                if (this.isWin && this.victoryCheckEnabled) {
                    return;
                }
            } else {
                gameView.showMessage(gameRole.toString() + " skip");
                this.lastTurnIsSkip = true;
                this.isWin = this.gameModel.isPlayerVictory(this.gameRole);
                if (this.isWin && this.victoryCheckEnabled) {
                    return;
                }
                break;
            }
        }
    }

    private void readTurns() throws IOException {
        for (var i = 0; i < 3; i++) {
            var gameTurn = new GameTurn(this.dataInputStream.readUTF());
            if (!gameTurn.isSkipTurn()) {
                gameModel.makeTurn(gameTurn.getColumn(), gameTurn.getRow(), this.oppositeGameRole);
                gameView.updateGameField();
                this.lastOppositeTurnIsSkip = false;
                this.isLose = this.gameModel.isPlayerVictory(this.oppositeGameRole);
                if (this.isLose && this.victoryCheckEnabled) {
                    return;
                }
            } else {
                gameView.showMessage(oppositeGameRole.toString() + " skip");
                this.lastOppositeTurnIsSkip = true;
                this.isLose = this.gameModel.isPlayerVictory(this.oppositeGameRole);
                if (this.isLose && this.victoryCheckEnabled) {
                    return;
                }
                break;
            }
        }
    }

    private void connectToServer() {
        try {
            this.gameView.showMessage("Connecting to server...");
            this.socket = new Socket(serverAddress, this.serverPort);
            this.dataInputStream = new DataInputStream(this.socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        } catch (Exception exception) {
            System.out.print(exception.getMessage());
        }
    }

    private void startServer() {
        try {
            this.gameView.showMessage("Waiting for client...");
            var serverSocket = new ServerSocket(serverPort, 0, null);
            this.socket = serverSocket.accept();
            this.dataInputStream = new DataInputStream(this.socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
        } catch (IOException exception) {
            System.out.print(exception.getMessage());
        }
    }
}
