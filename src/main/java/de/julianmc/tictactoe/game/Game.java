package de.julianmc.tictactoe.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Game {

    // board[x][y] represents a TicTacToe board
    // > where 0 means - the field is not marked
    // > where 1 means - the field is marked by the first player
    // > where 2 means - the field is marked by the second player
    private final int[][] board = new int[3][3];

    private final Player first, second;

    private final Location[] fieldLocations;

    private Player lastMarker;

    public Game(Location lowerBound, Location higherBound, Player first, Player second) {
        this.first = first;
        this.second = second;

        for (int x = 0; x < 3; x++) {
            Arrays.fill(board[x], 0);
        }

        this.fieldLocations = checkLocations(lowerBound, higherBound);
    }

    private Location[] checkLocations(Location lower, Location higher) {
        if (lower.getWorld() != higher.getWorld()) {
            throw new IllegalStateException("Both positions have different worlds");
        }
        World world = lower.getWorld();

        int x1 = lower.getBlockX(), x2 = higher.getBlockX();
        int y1 = lower.getBlockY(), y2 = higher.getBlockY();
        int z1 = lower.getBlockZ(), z2 = higher.getBlockZ();

        if (x1 + 3 != x2 || y1 + 3 != y2 || z1 != z2) {
            throw new IllegalStateException("Positions are invalid");
        }
        Location[] locations = new Location[9];
        int i = 0;
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                Block block = world.getBlockAt(x, y, z1);
                block.setType(Material.AIR);
                locations[i++] = block.getLocation();
            }
        }
        return locations;
    }

    /**
     * Returns if the board is full and there are no possible moves
     */
    public boolean isBoardFull() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (board[x][y] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWinnerPresent() {
        for (int i = 0; i < 3; i++) {
            // Check if there is a right row (vertical)
            // For example:
            // |x|x|x|
            // |-|-|-|
            // |-|-|-|
            boolean row = isLineMarkedSame(board[i][0], board[i][1], board[i][2]);
            // Check if there is right column (horizontal)
            // For example:
            // |x|-|-|
            // |x|-|-|
            // |x|-|-|
            boolean column = isLineMarkedSame(board[0][i], board[1][i], board[2][i]);

            if (row || column) {
                return true;
            }
        }
        // Check if there is a right diagonal (diagonal)
        // Which there are only two:
        // |-|-|x|
        // |-|x|-|
        // |x|-|-|
        //
        // And...
        // |x|-|-|
        // |-|x|-|
        // |-|-|x|
        boolean firstDiag = isLineMarkedSame(board[0][0], board[1][1], board[2][2]),
               secondDiag = isLineMarkedSame(board[0][2], board[1][1], board[2][0]);

        return firstDiag || secondDiag;
    }

    private boolean isLineMarkedSame(int pos1, int pos2, int pos3) {
        return pos1 != 0 && pos1 == pos2 && pos1 == pos3;
    }

    public boolean isIngame(Player player) {
        return player == first || player == second;
    }

    public int[][] getBoard() {
        return board;
    }

    public Location[] getFieldLocations() {
        return fieldLocations;
    }

    public Player getLastMarker() {
        return lastMarker;
    }

    public void setLastMarker(Player lastMarker) {
        this.lastMarker = lastMarker;
    }
}
