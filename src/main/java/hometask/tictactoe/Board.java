package hometask.tictactoe;

import java.util.Arrays;


public class Board {
    private static final int DEFAULT_SIZE = 3;

    private final Mark[][] board;

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
        this.board = other.toArray();
    }

    public int size() {
        return board.length;
    }

    public Mark[][] toArray() {
        Mark[][] copyBoard = new Mark[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(board[row], 0, copyBoard[row], 0, board.length);
        }
        return copyBoard;
    }

    public boolean isEmpty(Mark mark) {
        return mark == Mark.EMPTY;
    }

    boolean checkBoarders(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    boolean checkMark(Mark mark, int row, int col) {
        if (!(checkBoarders(row, col))) return false;
        return board[row][col] == mark;
    }
    public boolean place(int row, int col, Mark mark) {
        if (mark != null && !isEmpty(mark)) {
            if (checkBoarders(row, col)) {
                if (checkMark(Mark.EMPTY, row, col)) {
                    board[row][col] = mark;
                    return true;
                }
            }
        }
        return false;
    }

    public void clearBordPlace(int row, int col) {
        if (checkBoarders(row, col)) {
            board[row][col] = Mark.EMPTY;
        }
    }


    public boolean full() {
        int fl = 1;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
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
