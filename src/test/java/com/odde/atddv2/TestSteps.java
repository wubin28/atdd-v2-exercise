package com.odde.atddv2;

import io.cucumber.java.After;
import io.cucumber.java.zh_cn.假如;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.By.xpath;

@SpringBootTest
public class TestSteps {
    private WebDriver webDriver = null;

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    @After
    public void closeBrowser() {
        getWebDriver().quit();
    }

    @当("测试环境")
    public void 测试环境() {
        getWebDriver().get("http://host.docker.internal:10081/");
        assertThat(getWebDriver().findElements(xpath("//*[text()='登录']"))).isNotEmpty();
        getWebDriver().quit();
    }

    @那么("打印Token")
    public void 打印_token() {
    }

    @那么("打印百度为您找到的相关结果数")
    public void 打印百度为您找到的相关结果数() {
    }

    @假如("存在用户名为{string}和密码为{string}的用户")
    public void 存在用户名为和密码为的用户(String username, String password) {
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        userRepository.save(user);
    }

    @当("通过API以用户名为{string}和密码为{string}登录时")
    public void 通过api以用户名为和密码为登录时(String username, String password) {
        // Create OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create request body with JSON format
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "{\"userName\":\"" + username + "\", \"password\":\"" + password + "\"}");

        // Build and send the request
        Request request = new Request.Builder()
                .url("http://localhost:10081/users/login")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Unexpected response " + response);
            }

            // Store the response token for the next step.
            // In a real scenario, you might store it in a class member variable or context to be accessed in the next step.
            String token = response.header("Token");
            System.out.println("Received token: " + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @当("在百度搜索关键字{string}")
    public void 在百度搜索关键字(String arg0) {
    }

    private WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }
}
