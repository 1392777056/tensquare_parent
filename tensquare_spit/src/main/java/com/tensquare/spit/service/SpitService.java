package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/10/5 19:10
 *
 * 吐槽的业务层
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     * @return
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 根据id去查询
     * @param id
     * @return
     */
    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }

    /**
     * 新增吐槽
     * @param spit
     */
    public void add(Spit spit) {
        spit.set_id(String.valueOf(idWorker.nextId()));
        // 各种设置
        spit.setComment(0);
        spit.setVisits(0);
        spit.setThumbup(0);
        spit.setPublishtime(new Date());
        // 判断吐槽中是否有父id ，有父id 需要父id查询出来吐槽对象，并更新它的回复数
        if (!StringUtils.isEmpty(spit.getParentid())) {
            // 根据父id查询吐槽
            Spit parentSpit = spitDao.findById(spit.getParentid()).get();
            // 设置parentSpit回复数  自增1
            parentSpit.setComment(spit.getComment()+1);
            // 更新parentSpit
            spitDao.save(parentSpit);
        }
        spitDao.save(spit);
    }

    /**
     * 更新
     * @param spit
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 删除id
     * @param id
     */
    public void delete(String id) {
        spitDao.deleteById(id);
    }

    /**
     * 根据父级id查询吐槽
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentid,int page,int size) {
        // 创建分页对象
        PageRequest pageRequest = PageRequest.of(page,size);
        // 执行查询并返回
        return spitDao.findByParentid(parentid,pageRequest);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 吐槽点赞
     *
     * 要求一个月之内不能重复点赞
     * @param id
     */
    public void updateThumbup(String id) {
        // 获取用户id (此处我们没法实现，是因为用户的微服务问题)
        String userid = "1011";
        // 去redis缓存中查看是否有过点赞行为
        Object value = redisTemplate.opsForValue().get("spit_" + userid + "_" + id);
        if (value != null) {
            throw new RuntimeException("请不要重复点赞");
        }

        // 创建查询对象   作用是：用于确定更新那条数据
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        // 创建更新对象   作用是：用于确定更新什么内容
        Update update = new Update();
        // 每次增加1
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
        // 然后在存   （设置有效时间）
        redisTemplate.opsForValue().set("spit_" + userid + "_" + id,"thumbup",30, TimeUnit.DAYS);
    }
}
