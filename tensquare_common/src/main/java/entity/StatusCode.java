package entity;

import java.io.Serializable;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/9/24 12:23
 */
public class StatusCode implements Serializable {

    public static final int OK = 20000; //成功
    public static final int ERROR = 20001; //失败
    public static final int LOGINERROR = 20002; //用户名或密码错误
    public static final int ACCESSERROR = 20003; //权限不足
    public static final int REMOTEERROR = 20004; //远程调用失败
    public static final int REPERROR = 20005; //重复操作

}
