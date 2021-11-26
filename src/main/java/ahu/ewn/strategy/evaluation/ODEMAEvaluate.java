package ahu.ewn.strategy.evaluation;

/*
 * 创建时间：2017/01/10
 * 修改时间：2017/01/10
 */

import ahu.ewn.board.ChessBoard;
import ahu.ewn.board.Piece;
import ahu.ewn.board.PieceType;
import ahu.ewn.board.Point;


/**
 * 2016计算机博弈大赛使用的评估函数
 *
 * @author 陆梦轩
 *
 */
public class ODEMAEvaluate extends EvaluationFunction{

	/**
	 * 棋盘
	 */
	private ChessBoard board;

	/**
	 * 存储信息：棋子是否在当前棋盘上<br>
	 * isInBoard[i]=true 表示ID为i的棋子在board上<br>
	 * isInBoard[i]=false 表示ID为i的棋子不在board上
	 */
	private boolean isInBoard[];

	/**
	 * 构造函数
	 */
	public ODEMAEvaluate(){
		isInBoard=new boolean[27];
		setLabel("ODEMAEvaluate");
	}
	@Override
	public double getValue(ChessBoard board, PieceType type) {
		PieceType win=board.getWinner();
		if(win==type) return 100;
		else if(win!=type && win!=PieceType.NULL) return -100;

		this.board=board.clone();
		for(int i=0;i<27;i++){
			isInBoard[i]=false;
		}
		for(int x=0;x<5;x++){
			for(int y=0;y<5;y++){
				isInBoard[board.getPieceByPoint(x,y)]=true;
			}
		}

		PieceType me=type;
		PieceType enemy=(me==PieceType.BLUE? PieceType.RED:PieceType.BLUE);
		return 6*calculateDistance(me)-1*calculateThreat(enemy)-3*calculateDistance(enemy)+1*calculateThreat(me);
	}


	/**
	 * 计算指定棋子的出子概率
	 *
	 * @param piece 棋子
	 * @return 骰子骰到能使该棋子移动的点数的概率
	 */
	public double calculateProbability(byte piece){
		double p=(double)1/6.0d;
		byte i=0;
		for(i=piece;isInBoard[i]==false && ((11<=i && i<=16) || (21<=i && i<=26));i++);
		p+=(i-piece+1)*(1/6.0)/2.0;
		for(i=piece;isInBoard[i]==false && ((11<=i && i<=16) || (21<=i && i<=26));i--);
		p+=(piece-i+1)*(1/6.0)/2.0;
		return p;
	}

	/**
	 * 计算指定一方到达另一方大本营的概率*距离和
	 *
	 * @param type 棋子类型
	 * @return 概率*距离和
	 */
	public double calculateDistance(PieceType type){
		if(type==PieceType.BLUE) return calculateBlueDistance();
		else return calculateRedDistance();
	}

	/**
	 * 计算红方每个棋子到蓝方大本营的概率*距离和
	 *
	 * @return 概率*距离
	 */
	public double calculateRedDistance() {
		// TODO 自动生成的方法存根
		double E=0;
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(Piece.getPieceType(board.getPieceByPoint(i,j))!=PieceType.RED) continue;
				E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(board.getPieceByPoint(i,j));
			}
		}
		return E;
	}

	/**
	 * 计算蓝方每个棋子到红方大本营的概率*距离和
	 *
	 * @return 概率*距离
	 */
	public double calculateBlueDistance() {
		// TODO 自动生成的方法存根
		double E=0;
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(Piece.getPieceType(board.getPieceByPoint(i,j))!=PieceType.BLUE) continue;
				E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(board.getPieceByPoint(i,j));
			}
		}
		return E;
	}

	/**
	 * 计算指定棋子的价值
	 *
	 * @param piece 棋子
	 * @return 价值
	 */
	public double calculatePieceValue(byte piece){
		Point p=new Point(0,0);
		//初始位置，价值为0
		double value = 1.0;
		if (Piece.getPieceType(piece)==PieceType.RED)//红子
		{

			p.setX(board.getPointById(piece).x);
			p.setY(board.getPointById(piece).y);


		}
		else //蓝子
		{
			p.x = 4 - board.getPointById(piece).x;
			p.y = 4 - board.getPointById(piece).y;
		}
		if (p.x == 0 && p.y == 0)
		{
			return value = 0;
		}
		int t = Math.min(p.x, p.y);
		value = Math.pow(2, t);
		//边界位置特殊处理
		if (p.x == 4 && p.y != 0 && p.y != 4 || p.y == 4 && p.x != 0 && p.x != 4)
		{
			value += (value / 4);
		}

		return value;
	}

	/**
	 * 计算指定一方的威胁度
	 *
	 * @param type 棋子类型
	 * @return 威胁度
	 */
	public double calculateThreat(PieceType type){
		if(type==PieceType.BLUE) return calculateBlueThreat();
		else return calculateRedThreat();
	}

	/**
	 * 计算红方威胁度
	 *
	 * @return 红方威胁度
	 */
	public double calculateRedThreat() {
		// TODO 自动生成的方法存根
		double E=0;
		 byte piece;
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(Piece.getPieceType(board.getPieceByPoint(i,j))!=PieceType.RED) continue;

				if(i+1<5){
					piece=board.getPieceByPoint(i+1,j);
					if(Piece.getPieceType(piece)==PieceType.BLUE){
						E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(piece);
					}
				}

				if(j+1<5){
					piece=board.getPieceByPoint(i,j+1);
					if(Piece.getPieceType(piece)==PieceType.BLUE){
						E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(piece);
					}
				}

				if(i+1<5 && j+1<5){
					piece=board.getPieceByPoint(i+1,j+1);
					if(Piece.getPieceType(piece)==PieceType.BLUE){
						E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(piece);
					}
				}
			}
		}
		return E;
	}

	/**
	 * 计算蓝方威胁度
	 *
	 * @return 蓝方威胁度
	 */
	public double calculateBlueThreat() {
		// TODO 自动生成的方法存根
		double E=0;
		byte piece;
		for(int i=0;i<5;i++){
			for(int j=0;j<5;j++){
				if(Piece.getPieceType(board.getPieceByPoint(i,j))!=PieceType.BLUE) continue;

				if(i-1>=0){
					piece=board.getPieceByPoint(new Point(i-1,j));
					if(Piece.getPieceType(piece)==PieceType.RED){
						E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(piece);
					}
				}

				if(j-1>=0){
					piece=board.getPieceByPoint(i,j-1);
					if(Piece.getPieceType(piece)==PieceType.RED){
						E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(piece);
					}
				}

				if(i-1>=0 && j-1>=0){
					piece=board.getPieceByPoint(i-1,j-1);
					if(Piece.getPieceType(piece)==PieceType.RED){
						E+=calculatePieceValue(board.getPieceByPoint(i,j))*calculateProbability(piece);
					}
				}
			}
		}
		return E;
	}


}
