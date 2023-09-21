package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.entity.OrderLine;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.zh_cn.并且;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import org.springframework.beans.factory.annotation.Autowired;

import javax.json.Json;
import javax.json.JsonObject;

import javax.transaction.Transactional;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class ApiOrderSteps {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private Api api;

    @当("API查询订单时")
    public void api查询订单时() {
        api.get("orders");
    }

    @那么("返回如下订单")
    public void 返回如下订单(String json) {
        api.responseShouldMatchJson(json);
    }

    @并且("存在订单{string}的订单项:")
    @Transactional
    public void 存在订单的订单项(String orderCode, DataTable table) {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = orderRepo.findByCode(orderCode);
        table.asMaps().forEach(map -> order.getLines().add(objectMapper.convertValue(map, OrderLine.class).setOrder(order)));
        orderRepo.save(order);
    }

    @当("API查询订单{string}详情时")
    public void api查询订单详情时(String orderCode) {
        api.get("orders/"+orderCode);
    }

    @当("通过API发货订单{string}，快递单号为{string}")
    public void 通过api发货订单快递单号为(String orderCode, String deliveryCode) throws IOException {
        api.post("orders/"+orderCode+"/deliver", "{\"deliverNo\": \""+deliveryCode+"\"}");
//        JsonObject jsonObject = Json.createObjectBuilder()
//                .add("deliveryCode", deliveryCode).build();
//        api.post("orders/"+orderCode+"/deliver", jsonObject.toString());
    }

    @那么("订单{string}已发货，快递单号为{string}")
    public void 订单已发货快递单号为(String orderCode, String deliveryCode) {
        Order order = orderRepo.findByCode(orderCode);
        assertThat(order.getDeliverNo()).isEqualTo(deliveryCode);
        assertThat(order.getStatus()).isEqualTo(Order.OrderStatus.delivering);
    }
}
