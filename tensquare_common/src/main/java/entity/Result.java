package entity;

import java.io.Serializable;

/**
 * Create with www.dezhe.com
 *                             -----------  返回结果
 * @Author 德哲
 * @Date 2018/9/11 21:18
 */
public class Result implements Serializable {

    private boolean flag; //是否成功
    private Integer code; //状态码
    private String message; //响应的信息
    private Object data; //响应的数据，它可能有，也可能没有。（增删改没有，查询就有）

    public Result() {
    }

    /**
     * 增删改方法用的
     * @param flag
     * @param code
     * @param message
     */
    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    /**
     * 查询方法用的
     * @param flag
     * @param code
     * @param message
     * @param data
     */
    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
