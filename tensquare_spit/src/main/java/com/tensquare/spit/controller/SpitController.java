package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/10/5 19:19
 *
 * 吐槽的表现层控制器
 */
@RestController
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Spit> spits = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",spits);
    }

    /**
     * 根据id查询
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id){
        Spit spit = spitService.findById(id);
        return new Result(true, StatusCode.OK,"查询成功",spit);
    }

    /**
     * 保存
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    /**
     * 更新
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result save(@PathVariable(value = "id") String id,@RequestBody Spit spit){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id){
        spitService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**
     * 根据父id去查询吐槽列表
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable("parentid") String parentid,@PathVariable("page") int page,@PathVariable("size") int size) {
        Page<Spit> spitPage = spitService.findByParentid(parentid,page,size);
        PageResult<Spit> pageResult = new PageResult<>(spitPage.getTotalElements(),spitPage.getContent());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }

    /**
     * 吐槽点赞
     * @param id
     * @return
     */
    @RequestMapping(value = "/thumbup/{id}",method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable("id") String id){
        spitService.updateThumbup(id);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
