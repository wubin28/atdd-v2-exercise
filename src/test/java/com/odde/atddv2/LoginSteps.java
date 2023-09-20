package com.odde.atddv2;

import io.cucumber.java.After;
import io.cucumber.java.zh_cn.当;
import io.cucumber.java.zh_cn.那么;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.openqa.selenium.By.xpath;

public class LoginSteps {
    private WebDriver webDriver = null;

    @SneakyThrows
    public WebDriver createWebDriver() {
        return new RemoteWebDriver(new URL("http://web-driver.tool.net:4444"), DesiredCapabilities.chrome());
    }

    @当("以用户名为{string}和密码为{string}登录时")
    public void 以用户名为和密码为登录时(String userName, String password) {
        getWebDriver().get("http://host.docker.internal:10081");
        inputByPlaceholder(userName, "用户名");
        inputByPlaceholder(password, "密码");
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[text()=\"登录\"]")), Objects::nonNull).click();
    }

    private void inputByPlaceholder(String value, String placeholder) {
        await().ignoreExceptions().until(() -> getWebDriver().findElement(xpath("//*[@placeholder=\"" + placeholder + "\"]")), Objects::nonNull).sendKeys(value);
    }


    @那么("{string}登录成功")
    public void 登录成功(String userName) {
        await().ignoreExceptions().untilAsserted(() -> assertThat(getWebDriver().findElements(xpath("//*[text()='" + ("Welcome " + userName) + "']"))).isNotEmpty());
    }

    @那么("登录失败的错误信息是{string}")
    public void 登录失败的错误信息是(String message) {
        await().ignoreExceptions().untilAsserted(() -> assertThat(getWebDriver().findElements(xpath("//*[text()='" + message + "']"))).isNotEmpty());
    }
    public WebDriver getWebDriver() {
        if (webDriver == null)
            webDriver = createWebDriver();
        return webDriver;
    }

    @After
    public void quitWebDriver() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
        }
    }
}
