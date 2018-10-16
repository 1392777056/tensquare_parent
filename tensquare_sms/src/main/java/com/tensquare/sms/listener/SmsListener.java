package com.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/10/16 22:02
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Value("{template_code}")
    private String template_code;

    @Value("{sign_name}")
    private String sign_name;

    @Autowired
    private SmsUtil smsUtil;

    @RabbitHandler
    public void sendSms(Map<String,String> map) {
        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println(mobile+","+code);
        try {
            // 注意：这里的模板code需要做处理 Json字符串  ${code} ==> "{"code":"123"}"
            smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+code+"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
