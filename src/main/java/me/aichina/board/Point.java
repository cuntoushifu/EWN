package me.aichina.board;

/*
 * 创建时间：2017/01/09
 * 修改时间：2017/01/09
 */

/**
 * 棋盘上的坐标，规定左上角坐标为(0,0)
 *
 * @author 陆梦轩
 *
 */
public class Point {
    /**
     * 横坐标，从左向右递增
     */
    public int x;
    /**
     * 纵坐标，从上向下递增
     */
    public int y;

    public Point() {
    }

    /**
     * 构造函数
     *
     * @param x
     *            横坐标
     * @param y
     *            纵坐标
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return new String("("+this.x+","+this.y+")");
    }
}
