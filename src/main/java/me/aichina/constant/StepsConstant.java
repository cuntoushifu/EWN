package me.aichina.constant;

/**
 * @author YangYe
 * @email imissyou5201314@outlook.com
 * @date 2020/10/31
 */
public enum  StepsConstant {
    steps(6000,"搜索次数");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

   StepsConstant(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
