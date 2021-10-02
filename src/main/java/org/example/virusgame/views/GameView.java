package org.example.virusgame.views;

import org.example.virusgame.models.GameRole;

import java.util.Scanner;

public class GameView {
    Scanner scanner = new Scanner(System.in);

    public GameRole getGameRole() {
        while (true) {
            try {
                System.out.print("Game role [SERVER, CLIENT]: ");
                return GameRole.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException ignore) {}
        }
    }

    public String getServerIp() {
        while (true) {
            try {
                System.out.print("Server IP: ");
                return scanner.nextLine();
            } catch (IllegalArgumentException ignore) {}
        }
    }
}
