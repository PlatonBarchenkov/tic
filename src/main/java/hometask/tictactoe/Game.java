package hometask.tictactoe;

import java.util.Arrays;

public class Game {
    private static final int VECTOR_WON = 4;
    private final Board board;
    private final int winLen;
    private Move[] moveHistory;
    private GameState gameState;

    public Game() {
        this(3);
    }

    public Game(int size) {
        this(size, size);
    }

    public Game(int size, int winLength) {
        winLen = winLength;
        board = new Board(size);
        moveHistory = new Move[0];
        gameState = GameState.X_TURN;
    }

    public Game(Game other) {
        winLen = other.winLen;
        board = new Board(other.board);
        moveHistory = Arrays.copyOf(other.moveHistory, other.moveHistory.length);
        gameState = other.gameState;
    }

    public Board board() {
        return new Board(board);
    }

    public Move[] history() {
        return moveHistory.clone();
    }

    public boolean wonCheck(Mark mark, int coefCol, int coefRow) {
        int countMark = 0;
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board().chekMark(mark, row, col)) {
                    countMark = 1;
                    for (int len = 1; len < winLen + 1; len++) {
                        if (!board().chekMark(mark, row + coefCol * len, col + coefRow * len)) {
                            break;
                        }
                        countMark++;
                    }
                }
                if (countMark >= winLen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasWonMark(Mark mark) {
        return wonCheck(mark, 0, 1) || wonCheck(mark, 1, 0) || wonCheck(mark, 1, 1) || wonCheck(mark, 1, -1);
    }

    public void changeStateAfterWin() {
        if (hasWonMark(Mark.X)) {
            gameState = GameState.X_WON;
        } else if (hasWonMark(Mark.O)) {
            gameState = GameState.O_WON;
        }
    }

    public GameState state() {
        changeStateAfterWin();
        if (gameState == GameState.O_WON) {
            return GameState.O_WON;
        } else if (gameState == GameState.X_WON) {
            return GameState.X_WON;
        }
        if (gameState == GameState.X_TURN) {
            if (!(board.full())) {
                return GameState.X_TURN;
            }
        } else {
            if (!(board.full())) {
                return GameState.O_TURN;
            }
        }
        if (board.full()) {
            return GameState.DRAW;
        }
        return (gameState == GameState.O_TURN) ? GameState.X_TURN : GameState.O_TURN;
    }

    private boolean canApply(Move move) {
        gameState = state();
        if (!(moveHistory.length == 0 && move.mark() == Mark.O)) {
            if (gameState != GameState.O_WON && gameState != GameState.X_WON && gameState != GameState.DRAW) {
                if (board.place(move.row(), move.col(), move.mark())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void changeState(GameState State) {
        if (State == GameState.O_TURN) {
            gameState = GameState.X_TURN;
        }
        if (State == GameState.X_TURN) {
            gameState = GameState.O_TURN;
        }
    }

    private void changeState(Mark mark) {
        if (mark == Mark.O) {
            gameState = GameState.O_TURN;
        }
        if (mark == Mark.X) {
            gameState = GameState.X_TURN;
        }
    }

    public boolean apply(Move move) {
        gameState = state();
        if (board.chekMark(Mark.EMPTY, move.row(), move.col())) {
            if (canApply(move)) {
                changeState(gameState);
                moveHistory = Arrays.copyOf(moveHistory, moveHistory.length + 1);
                moveHistory[moveHistory.length - 1] = new Move(move.row(), move.col(), move.mark());
                return true;

            }
        }
        return false;
    }

    public Move[] lastWinningLine() {
        Move[] moveWon = new Move[winLen];
        GameState state = state();
        Mark winner = null;
        int startRow;
        int startCol;
        int deltaRow;
        int deltaCol;
        int[] winLine = new int[VECTOR_WON];
        if (state != GameState.O_WON && state != GameState.X_WON) {
            return new Move[0];
        }
        if (state == GameState.O_WON) {
            winLine = lastWinningLine(Mark.O);
            winner = Mark.O;
        } else if (state == GameState.X_WON) {
            winLine = lastWinningLine(Mark.X);
            winner = Mark.X;
        }
        startRow = winLine[0];
        startCol = winLine[1];
        deltaRow = winLine[2];
        deltaCol = winLine[3];
        if (winLine != null) {
            for (int step = 0; step < winLen; step++) {
                moveWon[step] = new Move(startRow + deltaRow * step, startCol + deltaCol * step, winner);
            }
        } else {
            return new Move[0];
        }
        return moveWon;
    }

    private int[] lastWinningLine(Mark mark) {
        int[][] vectorWin = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] vector : vectorWin) {
            for (int row = 0; row < board.size(); row++) {
                for (int col = 0; col < board.size(); col++) {
                    if (board.chekMark(mark, row, col)) {

                        if (hasWinningLine(row, col, vector[0], vector[1], mark)) {
                            return new int[]{row, col, vector[0], vector[1]};
                        }
                    }
                }
            }
        }
        return null;
    }


    private boolean hasWinningLine(int row, int col, int coefFirst, int coefSecond, Mark mark) {
        for (int len = 1; len < winLen; len++) {
            if (!board.chekMark(mark, row + coefFirst * len, col + coefSecond * len)) {
                return false;
            }
        }
        return true;
    }

    public boolean undoLast() {
        if (moveHistory.length != 0) {
            changeState(moveHistory[moveHistory.length - 1].mark());
            board.clearBordPlace(moveHistory[moveHistory.length - 1].row(), moveHistory[moveHistory.length - 1].col());
            moveHistory = Arrays.copyOf(moveHistory, moveHistory.length - 1);
            return true;
        }
        return false;
    }

    public Game mergeFromLogs(Move[] a, Move[] b) {
        return null;
    }
}