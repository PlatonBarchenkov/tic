package hometask.tictactoe;

import java.util.Arrays;

public class Game {
    private final Board board;
    private final int winLen;
    private Move[] moveHistory;
    private GameState gameState;
    private static final int VECTOR_WON = 4;

    public Game() {
        this(3);
    }

    public Game(int size) {
        this(size, size);
    }

    Game(int size, int winLength) {
        winLen = winLength;
        board = new Board(size);
        moveHistory = new Move[0];
        gameState = GameState.X_TURN;
    }

    Game(Game other) {
        winLen = other.winLen;
        board = new Board(other.board);
        moveHistory = Arrays.copyOf(other.moveHistory, other.moveHistory.length);
        gameState = other.gameState;
    }

    Board board() {
        return new Board(board);
    }

    Move[] history() {
        return moveHistory.clone();
    }

    public boolean wonCheck(Mark mark, int coefCol, int coefRow) {
        int countMark = 0;
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board().checkMark(mark, row, col)) {
                    countMark = 1;
                    for (int len = 1; len < winLen + 1; len++) {
                        if (!board().checkMark(mark, row + coefCol * len, col + coefRow * len)) {
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

    boolean canApply(Move move) {
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

    void changeState(GameState gameState) {
        if (gameState == GameState.O_TURN) {
            this.gameState = GameState.X_TURN;
        }
        if (gameState == GameState.X_TURN) {
            this.gameState = GameState.O_TURN;
        }
    }

    void changeState(Mark mark) {
        if (mark == Mark.O) {
            this.gameState = GameState.O_TURN;
        }
        if (mark == Mark.X) {
            this.gameState = GameState.X_TURN;
        }
    }

    public boolean apply(Move move) {
        gameState = state();
        if (board.checkMark(Mark.EMPTY, move.row(), move.col())) {
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
        if (winLine != null && winner != null) {
            for (int len = 0; len < winLen; len++) {
                moveWon[len] = new Move(winLine[0] + winLine[2] * len, winLine[1] + winLine[3] * len, winner);
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
                    if (board.checkMark(mark, row, col)) {

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
            if (!board.checkMark(mark, row + coefFirst * len, col + coefSecond * len)) {
                return false;
            }
            if (!board.checkMark(mark, row + coefFirst, col + coefSecond)) {
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

    Game mergeFromLogs(Move[] a, Move[] b) {
        return null;
    }
}