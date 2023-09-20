package com.odde.atddv2.page;

import com.odde.atddv2.Browser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderPage {
    @Autowired
    private Browser browser;


    public void placeOrder(Map<String, String> orderData) {
        browser.clickByText("录入订单");
        browser.inputTextByPlaceholder("订单号", orderData.get("订单号"));
        browser.inputTextByPlaceholder("商品名称", orderData.get("商品名称"));
        browser.inputTextByPlaceholder("金额", orderData.get("金额"));
        browser.inputTextByPlaceholder("收件人", orderData.get("收件人"));
        browser.inputTextByPlaceholder("电话", orderData.get("电话"));
        browser.inputTextByPlaceholder("地址", orderData.get("地址"));
        browser.inputTextByPlaceholder("状态", orderData.get("状态"));

        browser.clickByText("提交");
    }

    public void showOrders(List<String> orderDetails){
        browser.shouldHaveText(orderDetails.get(0));
        browser.shouldHaveText(orderDetails.get(1));
        browser.shouldHaveText(orderDetails.get(2));
        browser.shouldHaveText(orderDetails.get(3));
    }
}
