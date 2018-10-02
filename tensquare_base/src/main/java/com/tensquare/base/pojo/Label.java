package com.tensquare.base.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/9/24 12:40
 *
 * 标签的实体类
 */
@Entity
@Table(name = "tb_label")
public class Label implements Serializable {

    @Id
    private String id;
    private String labelname;
    private String state;
    private Integer count;
    private Integer fans;
    private String recommend;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
