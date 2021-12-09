package edu.bfdestroyeer.virusgame.controllers;

import edu.bfdestroyeer.virusgame.models.GameModel;
import edu.bfdestroyeer.virusgame.models.GameRole;
import edu.bfdestroyeer.virusgame.models.GameTurn;
import edu.bfdestroyeer.virusgame.views.GameView;

import java.io.*;
import java.net.*;

public class GameController {

    private final GameModel gameModel;
    private final GameView gameView ;

    private GameRole gameRole;
    private GameRole oppositeGameRole;

    private Socket socket;
    private InetAddress serverAddress;
    private final int serverPort = 5020;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

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
            try {
                connectToServer();
            } catch (IOException exception) {
                this.gameView.showMessage("Server do not respond");
                return;
            }
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

            this.objectOutputStream.writeObject(gameTurn);
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
            GameTurn gameTurn;
            try {
                gameTurn = (GameTurn) this.objectInputStream.readObject();
            } catch (ClassNotFoundException exception) {
                System.out.println(exception.getMessage());
                throw new IOException();
            }
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

    private void connectToServer() throws IOException {

        this.gameView.showMessage("Connecting to server...");
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(serverAddress, serverPort), 1000);
        try {
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException exception) {
            System.out.print(exception.getMessage());
        }
    }

    private void startServer() {
        try {
            this.gameView.showMessage("Waiting for client...");
            var serverSocket = new ServerSocket(serverPort, 0, null);
            this.socket = serverSocket.accept();
            this.objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException exception) {
            System.out.print(exception.getMessage());
        }
    }
}
