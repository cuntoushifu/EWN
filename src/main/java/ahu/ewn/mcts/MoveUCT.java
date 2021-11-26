package ahu.ewn.mcts;

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.PieceType;
import ahu.ewn.constant.ParamConstant;
import ahu.ewn.game.Move;

/**
 * 封装走法胜率和搜索次数的实体类
 * 便于计算UCT值
 *
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/10/14
 */
public class MoveUCT implements Comparable<MoveUCT> {
    private Move move;
    private Integer winCount;
    private Double winRate;
    private Integer searchCount;
    private Integer totalCount;
    private Double UCTValue;
    private PieceType type;
    private ChessBoard board;

    public double calculateUCTValue(int finalTotal) {
        this.totalCount = finalTotal;
       // System.out.println("改良 WinRate = "+ParamConstant.Alpha.code *(winCount * 1.0) / (searchCount * 1.0)+" , deep = "+ParamConstant.Beta.code * Math.sqrt(Math.log(finalTotal) / searchCount));

        this.UCTValue = ParamConstant.Alpha.code *(winCount * 1.0) / (searchCount * 1.0) +
                ParamConstant.Beta.code * Math.sqrt(Math.log(finalTotal) / searchCount);
        return UCTValue;
    }

    public double calculateUCB(int finalTotal) {
        System.out.println("WinRate = "+winRate+" , deep = "+Math.sqrt(Math.log(finalTotal) / searchCount));
        this.totalCount = finalTotal;
        this.UCTValue =  (winCount * 1.0) / (searchCount * 1.0) +
                Math.sqrt(Math.log(finalTotal) / searchCount);
        return UCTValue;
    }
    @Override
    public int compareTo(MoveUCT o) {
        if (getUCTValue() >= o.getUCTValue()) return -1;
        if (getUCTValue() < o.getUCTValue()) return 1;
        return 0;
    }

    public MoveUCT(Move move, Integer winCount, Integer searchCount) {
        this.move = move;
        this.winCount = winCount;
        this.winRate = (winCount * 1.0) / (searchCount * 1.0);
        this.searchCount = searchCount;
    }

    public MoveUCT(Move move, Integer winCount, Integer searchCount, ChessBoard board, PieceType pieceType) {
        this.type = pieceType;
        this.board = board;
        this.winCount = winCount;
        this.move = move;
        this.winRate = (winCount * 1.0) / (searchCount * 1.0);
        this.searchCount = searchCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public void addSearchCount(int searchCount) {
        this.searchCount += searchCount;
    }

    public void addTotalCount(int totalCount) {
        this.totalCount += totalCount;
    }

    public void addWinCount(int winCount) {
        this.winCount += winCount;
    }





    public Integer getWinCount() {
        return winCount;
    }

    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    public Double getUCTValue() {
        return UCTValue;
    }

    public void setUCTValue(Double UCTValue) {
        this.UCTValue = UCTValue;
    }

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public Double getWinRate() {
        return winRate;
    }

    public void setWinRate(Double winRate) {
        this.winRate = winRate;
    }

    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }


    @Override
    public String toString() {
        return "MoveUCT{" +
                "move=" + move +
                ", winRate=" + winRate +
                ", currentTotalCount=" + searchCount +
                '}';
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }


}
