package com.odde.atddv2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.atddv2.entity.Order;
import com.odde.atddv2.entity.User;
import com.odde.atddv2.page.OrderPage;
import com.odde.atddv2.page.WelcomePage;
import com.odde.atddv2.repo.OrderRepo;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class OrderSteps {

    @Autowired
    private WelcomePage welcomePage;

    @Autowired
    private Browser browser;

    @Autowired
    private OrderPage orderPage;

    @Autowired
    private LoginSteps loginSteps;

    @Autowired
    private  OrderRepo orderRepo;
    private Response orderResponse;


    @SneakyThrows
    @那么("显示如下订单")
    public void 显示如下订单(DataTable table) {
        table.asList().forEach(browser::shouldHaveText);
    }

    @当("用如下数据录入订单:")
    public void 用如下数据录入订单(DataTable table) {
        welcomePage.goToOrders();
        orderPage.addOrder(table.asMaps().get(0));
    }

    @假如("存在如下订单:")
    public void 存在如下订单(DataTable table) {
        loginSteps.存在用户名为和密码为的用户("joseph", "123");
        Map<String, String> orderData = table.asMaps().get(0);
        Order order = new Order();
        order.setCode(orderData.get("code"));
        order.setProductName(orderData.get("productName"));
        order.setTotal(new BigDecimal(orderData.get("total")));
        order.setRecipientName(orderData.get("recipientName"));
        order.setStatus(Order.OrderStatus.valueOf(orderData.get("status")));

        orderRepo.save(order);

    }

    @SneakyThrows
    @当("API查询订单时")
    public void api查询订单时() {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(new User().setUserName("joseph").setPassword("123")));
        Request request = new Request.Builder().url("http://localhost:10081/users/login").post(requestBody).build();
        Response loginResponse = okHttpClient.newCall(request).execute();
        String token = loginResponse.header("token");

        Request orderRequest = new Request.Builder().url("http://localhost:10081/api/orders").addHeader("token", token).build();
        orderResponse = okHttpClient.newCall(orderRequest).execute();
    }

    @那么("返回如下订单")
    public void 返回如下订单(String expectedResponse) throws IOException, JSONException {

        JSONAssert.assertEquals(expectedResponse, orderResponse.body().string(), true);

    }
}
