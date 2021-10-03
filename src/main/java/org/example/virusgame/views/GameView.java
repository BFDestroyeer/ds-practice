package org.example.virusgame.views;

import org.example.virusgame.models.GameModel;
import org.example.virusgame.models.GameRole;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameView {
    GameModel gameModel;

    Scanner scanner = new Scanner(System.in);

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public GameRole requestGameRole() {
        while (true) {
            try {
                System.out.print("Game role [SERVER, CLIENT]: ");
                return GameRole.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException ignore) {}
        }
    }

    public InetAddress requestServerIp() {
        while (true) {
            try {
                System.out.print("Server IP: ");
                return InetAddress.getByName(scanner.nextLine());
            } catch (UnknownHostException ignore) {}
        }
    }

    public int[] reqestTurn(GameRole gameRole) {
        while (true) {
            try {
                System.out.print("Your turn (column row): ");
                var row = scanner.nextInt();
                var line = scanner.nextInt();
                this.gameModel.makeTurn(row, line, gameRole);
                return new int[] {row, line};
            } catch (IllegalArgumentException exception) {
                System.out.println("Illegal turn, try again");
            }
        }

    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void updateGameField() {
        var gameField = gameModel.getGameField();
        System.out.print("\033[H\033[2J");
        for (var row = 0; row < GameModel.FIELD_LENGTH; row++) {
            System.out.printf("%d│", row);
            for (var column = 0; column < GameModel.FIELD_LENGTH; column++) {
                switch (gameField[column][row]) {
                    case EMPTY -> {
                        System.out.print(" ");
                    }
                    case SERVER_ALIVE -> {
                        System.out.print("×");
                    }
                    case SERVER_KILLED -> {
                        System.out.print("⦻");
                    }
                    case CLIENT_ALIVE -> {
                        System.out.print("◯");
                    }
                    case CLIENT_KILLED -> {
                        System.out.print("⬤");
                    }
                }
            }
            System.out.print('\n');
        }
        System.out.println("─┴──────────");
        System.out.println("  0123456789");
    }
}
