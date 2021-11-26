package ahu.ewn.constant;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/10/31
 */
public enum ParamConstant {

    Alpha(0.4, "胜利参数"), Beta(1, "搜索深度参数");
    public double code;
    String msg;

    ParamConstant(double code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
