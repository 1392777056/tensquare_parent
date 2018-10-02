package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Create with www.dezhe.com
 *                             -----------  返回分页的结果集封装类
 * @Author 德哲
 * @Date 2018/9/11 21:18
 */
public class PageResult<T> implements Serializable {

    private Long total; //总记录数
    private List<T> rows; //分页的结果集

    public PageResult() {
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
