package com.odde.atddv2;

import com.odde.atddv2.page.HomePage;
import com.odde.atddv2.page.OrderPage;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import org.springframework.beans.factory.annotation.Autowired;


public class OrderSteps {

    @Autowired
    private OrderPage orderPage;

    @Autowired
    private HomePage homePage;

    @当("用如下数据录入订单:")
    public void 用如下数据录入订单(io.cucumber.datatable.DataTable dataTable) {
        homePage.navigateToOrder();
        orderPage.placeOrder(dataTable.asMaps().get(0));
    }

    @那么("显示如下订单")
    public void 显示如下订单(io.cucumber.datatable.DataTable dataTable) {
        orderPage.showOrders(dataTable.asList());
    }


}
