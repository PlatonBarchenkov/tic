package hometask.tictactoe;

import java.util.Arrays;


public class Board {
    private static final int DEFAULT_SIZE = 3;

    final Mark[][] board;

    public Board() {
        this(DEFAULT_SIZE);
    }

    public Board(int size) {
        board = new Mark[size][size];
        for (int row = 0; row < size; row++) {
            Arrays.fill(board[row], Mark.EMPTY);
        }
    }

    public Board(Board other) {
        board = new Mark[other.board.length][other.board[0].length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                board[row][col] = other.board[row][col];
            }
        }
    }

    public int size() {
        return board.length;
    }

    public Mark[][] toArray() {
        Mark[][] copyBoard = new Mark[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                copyBoard[row][col] = board[row][col];
            }
        }
        return copyBoard;
    }

    public boolean place(int row, int col, Mark mark) {
        if (mark != null && mark != Mark.EMPTY) {
            if (!(row < 0 || row >= board.length || col < 0 || col >= board[0].length)) {
                if (board[row][col] == Mark.EMPTY) {
                    board[row][col] = mark;
                    return true;
                }
            }
        }
        return false;
    }

    public void clear(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return;
        board[row][col] = Mark.EMPTY;
    }


    public boolean full() {
        int fl = 1;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == Mark.EMPTY) {
                    fl = 0;
                    break;
                }
            }
        }
        if (fl == 1) {
            return true;
        } else {
            return false;
        }
    }

    public int[][] availableMoves() {
        int countEmpty = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == Mark.EMPTY) {
                    countEmpty++;
                }
            }
        }
        int[][] placeEmpty = new int[countEmpty][2];
        int indexEmpty = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == Mark.EMPTY) {
                    placeEmpty[indexEmpty] = new int[]{row, col};
                    indexEmpty++;
                }
            }
        }
        return placeEmpty;
    }
}
