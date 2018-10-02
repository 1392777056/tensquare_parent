package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/9/24 16:42
 *
 * 标签的业务层
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有标签
     * @return
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    /**
     * 保存
     * @param label
     */
    public void save(Label label) {
        //设置id
        label.setId(String.valueOf(idWorker.nextId()));
        labelDao.save(label);
    }

    /**
     * 更新
     * @param label
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     * 删除
     * @param id
     */
    public void delete(String id) {
        labelDao.deleteById(id);
    }

    /**
     * 条件查询
     * @return
     */
    public List<Label> findSearch(Label label) {
        // 1.定义条件对象
        Specification<Label> spec = new Specification<Label>() {
            /**
             * 拼装查询条件
             * @param root
             * @param cq
             * @param cb
             * @return
             * 条件是： 标签名称的模糊查询  状态精确查询  是否推荐的精确查询
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                ArrayList<Predicate> list = new ArrayList();
                // 判断输入了标签名称
                if (!StringUtils.isEmpty(label.getLabelname())) {
                    Predicate p1 = cb.like(root.get("labelname"), "%"+label.getLabelname()+"%");
                    list.add(p1);
                }
                // 判断了标签状态
                if (!StringUtils.isEmpty(label.getState())) {
                    Predicate p2 = cb.equal(root.get("state"), label.getState());
                    list.add(p2);
                }
                // 判断了标签的是否推荐
                if (!StringUtils.isEmpty(label.getRecommend())) {
                    Predicate p3 = cb.like(root.get("recommend"), label.getRecommend());
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };
        // 2.条件查询
        return labelDao.findAll(spec);
    }

    /**
     * 分页查询
     * @return
     */
    public Page<Label> findSearch(Label label, int page, int size) {
        // 1.定义条件对象
        Specification<Label> spec = new Specification<Label>() {
            /**
             * 拼装查询条件
             * @param root
             * @param cq
             * @param cb
             * @return
             * 条件是： 标签名称的模糊查询  状态精确查询  是否推荐的精确查询
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                ArrayList<Predicate> list = new ArrayList();
                // 判断输入了标签名称
                if (!StringUtils.isEmpty(label.getLabelname())) {
                    Predicate p1 = cb.like(root.get("labelname"), "%"+label.getLabelname()+"%");
                    list.add(p1);
                }
                // 判断了标签状态
                if (!StringUtils.isEmpty(label.getState())) {
                    Predicate p2 = cb.equal(root.get("state"), label.getState());
                    list.add(p2);
                }
                // 判断了标签的是否推荐
                if (!StringUtils.isEmpty(label.getRecommend())) {
                    Predicate p3 = cb.like(root.get("recommend"), label.getRecommend());
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[list.size()]));
            }
        };

        // 因为它从0 开始
        PageRequest pageRequest = PageRequest.of(page-1,size);
        // 2.条件查询
        return labelDao.findAll(spec,pageRequest);
    }

}
