package org.example.virusgame.controllers;

import org.example.virusgame.models.GameModel;
import org.example.virusgame.models.GameRole;
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

    private Socket socket;
    private InetAddress serverAddress;
    private final int serverPort = 8080;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public GameController() {
        this.gameModel = new GameModel();
        this.gameView = new GameView(this.gameModel);
    }

    public void start() {
        this.gameRole = gameView.requestGameRole();
        if (gameRole == GameRole.SERVER) {
            startServer();
        } else if (gameRole == GameRole.CLIENT) {
            this.serverAddress = gameView.requestServerIp();
            connectToServer();
        }

        try {
            gameView.updateGameField();
            if (this.gameRole == GameRole.CLIENT) {
                readTurns();
                gameView.updateGameField();
            }
            while (true) {
                for (var i = 0; i < 3; i++) {
                    var turn = gameView.reqestTurn(this.gameRole);
                    gameView.updateGameField();
                    dataOutputStream.writeInt(turn[0]);
                    dataOutputStream.writeInt(turn[1]);
                }
                readTurns();
                gameView.updateGameField();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void readTurns() throws IOException {
        for (var i = 0; i < 3; i++) {
            var row = dataInputStream.readInt();
            var line = dataInputStream.readInt();
            if (gameRole == GameRole.SERVER) {
                gameModel.makeTurn(row, line, GameRole.CLIENT);
            } else if (gameRole == GameRole.CLIENT) {
                gameModel.makeTurn(row, line, GameRole.SERVER);
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

    private void makeTurn() {

    }
}
