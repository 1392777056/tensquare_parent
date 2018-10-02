package com.tensquare.base.web;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
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
 * @Date 2018/9/28 21:00
 *
 * 标签控制器
 */
@RestController
@RequestMapping("/label")
@CrossOrigin  // 当前的控制器支持跨域访问
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * 查询所有
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Label> labels = labelService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",labels);
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id) {
        Label label = labelService.findById(id);
        return new Result(true, StatusCode.OK,"查询id成功",label);
    }

    /**
     * 保存
     * @param label
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label) {
        labelService.save(label);
        return new Result(true, StatusCode.OK,"保存成功");
    }

    /**
     * 更新
     * @param label
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable("id") String id,@RequestBody Label label) {
        label.setId(id);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"更新成功");
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable("id") String id) {
        labelService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    /**
     * 条件查询
     * @param label
     * @return
     */
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Label label) {
        // 1.查询的数据
        List<Label> labels = labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",labels);
    }

    /**
     * 条件查询带分页
     * @param label
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Label label,@PathVariable("page") int page,@PathVariable("size") int size) {
        // 1.查询的数据
        Page<Label> labelPage = labelService.findSearch(label,page,size);
        PageResult<Label> pageResult = new PageResult(labelPage.getTotalElements(),labelPage.getContent());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);

    }

}
