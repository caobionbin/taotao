package com.taotao.web.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.httpclient.HttpResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by dd876799869 on 2017/7/15.
 */
@Service
public class OrderService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_ORDER_URL}")
    public  String TAOTAO_ORDER_URL;

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 提交订单
     * @return
     */
    public String submitOrder(Order order) {
        try {
            //调用taotao-order系统的提交订单接口
            String orderUrl = TAOTAO_ORDER_URL + "/order/create";
            String json =  mapper.writeValueAsString(order);
            HttpResult httpResult =  apiService.doPostJson(orderUrl,json);
            if (httpResult.getCode().intValue() == 200){
                //响应的状态码200  将httpResult发序列化成对象
               JsonNode jsonNode = mapper.readTree(httpResult.getBody());
                if (jsonNode.get("status").intValue() == 200){
                    //业务状态码200  创建订单成功
                    return jsonNode.get("data").asText();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
