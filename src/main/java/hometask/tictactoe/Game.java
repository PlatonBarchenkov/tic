package hometask.tictactoe;

import java.util.Arrays;

public class Game {
    Board board;
    int winLen;
    Move[] moveHistory;
    int moveCount;
    GameState gameState;

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
        moveCount = 0;
        gameState = GameState.X_TURN;
    }

    Game(Game other) {
        winLen = other.winLen;
        board = new Board(other.board);
        moveHistory = Arrays.copyOf(other.moveHistory, other.moveHistory.length);
        moveCount = other.moveCount;
        gameState = other.gameState;
    }

    Board board() {
        return new Board(board);
    }

    Move[] history() {
        return moveHistory.clone();
    }

    public boolean wonHorizon(Mark mark) {
        int countMark = 0;
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board.board[row][col] == mark) {
                    countMark = 1;
                    for (int len = 1; len < winLen + 1; len++) {
                        if (col + len < board.size() && col + len >= 0) {
                            if (board.board[row][col + len] != mark) {
                                break;
                            }
                            countMark++;
                        }
                    }
                }
                if (countMark >= winLen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean wonVertical(Mark mark) {
        int countMark = 0;
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board.board[row][col] == mark) {
                    countMark = 1;
                    for (int len = 1; len < winLen + 1; len++) {
                        if (row + len < board.size() && row + len >= 0) {
                            if (board.board[row + len][col] != mark) {
                                break;
                            }
                            countMark++;
                        }
                    }
                }
                if (countMark >= winLen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean wonDiagRight(Mark mark) {
        int countMark = 0;
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board.board[row][col] == mark) {
                    countMark = 1;
                    for (int len = 1; len < winLen + 1; len++) {
                        if (row + len < board.size() && row + len >= 0 && col + len < board.size() && col + len >= 0) {
                            if (board.board[row + len][col + len] != mark) {
                                break;
                            }
                            countMark++;
                        }
                    }
                }
                if (countMark >= winLen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean wonDiagLeft(Mark mark) {
        int countMark = 0;
        for (int row = 0; row < board.size(); row++) {
            for (int col = 0; col < board.size(); col++) {
                if (board.board[row][col] == mark) {
                    countMark = 1;
                    for (int len = 1; len < winLen + 1; len++) {
                        if (row + len < board.size() && row + len >= 0 && col - len < board.size() && col - len >= 0) {
                            if (board.board[row + len][col - len] != mark) {
                                break;
                            }
                            countMark++;
                        }
                    }
                }
                if (countMark >= winLen) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean won(Mark mark) {
        return (wonHorizon(mark) || wonVertical(mark) || wonDiagRight(mark) || wonDiagLeft(mark));
    }


    public GameState state() {
        if (moveCount % 2 == 0) {
            if (won(Mark.O)) {
                return GameState.O_WON;
            } else if (!(board.full())) {
                return GameState.X_TURN;
            }
        } else {
            if (won(Mark.X)) {
                return GameState.X_WON;
            } else if (!(board.full())) {
                return GameState.O_TURN;
            }
        }
        if (board.full()) {
            if (won(Mark.X)) {
                return GameState.X_WON;
            }
            if (won(Mark.O)) {
                return GameState.O_WON;
            }
            return GameState.DRAW;
        }
        return (moveCount % 2 == 0) ? GameState.X_TURN : GameState.O_TURN;
    }

    public boolean apply(Move move) {
        gameState = state();
        if (move.col() < board.size() && move.col() >= 0 && move.row() >= 0 && move.row() < board.size() && (move.mark() == Mark.O || move.mark() == Mark.X)) {
            if ((moveCount % 2 == 0 && move.mark() == Mark.X) || (moveCount % 2 == 1 && move.mark() == Mark.O)) {
                if (board.board[move.row()][move.col()] == Mark.EMPTY) {
                    if (!(gameState == GameState.O_WON || gameState == GameState.X_WON || gameState == GameState.DRAW)) {
                        board.place(move.row(), move.col(), move.mark());
                        moveCount++;
                        moveHistory = Arrays.copyOf(moveHistory, moveHistory.length + 1);
                        moveHistory[moveHistory.length - 1] = new Move(move.row(), move.col(), move.mark());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Move[] lastWinningLine() {
        Move[] moveWon = new Move[winLen];
        GameState state = state();
        Mark winner = null;
        int[] winLine = new int[4];
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
        for (int[] v : vectorWin) {
            for (int row = 0; row < board.size(); row++) {
                for (int col = 0; col < board.size(); col++) {
                    if (board.board[row][col] == mark) {

                        if (hasWinningLine(row, col, v[0], v[1], mark)) {
                            return new int[]{row, col, v[0], v[1]};
                        }
                    }
                }
            }
        }
        return null;
    }


    private boolean hasWinningLine(int row, int col, int coefFirst, int coefSecond, Mark mark) {
        for (int len = 1; len < winLen; len++) {
            if (row + coefFirst * len >= board.size() || col + coefSecond * len >= board.size() || row + coefFirst * len < 0 || col + coefSecond * len < 0) {
                return false;
            }
            if (board.board[row + coefFirst * len][col + coefSecond * len] != mark) {
                return false;
            }
        }
        return true;
    }

    public boolean undoLast() {
        if (moveHistory.length != 0) {
            board.clear(moveHistory[moveHistory.length - 1].row(), moveHistory[moveHistory.length - 1].col());
            moveHistory = Arrays.copyOf(moveHistory, moveHistory.length - 1);
            moveCount--;
            gameState = state();
            return true;
        }
        return false;
    }

    Game mergeFromLogs(Move[] a, Move[] b) {
        return null;
    }
}