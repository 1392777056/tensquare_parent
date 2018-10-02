package com.tensquare.test;

import com.tensquare.base.BaseApplication;
import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/9/28 20:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BaseApplication.class)
public class TestBaseService {

    @Autowired
    private LabelService labelService;

    @Test
    public void toSave() {
        Label label = new Label();
        label.setLabelname("杨晨");
        labelService.save(label);
    }

    @Test
    public void getFindOne() {
        System.out.println(labelService.findById("1045658533196029952"));
    }

}
