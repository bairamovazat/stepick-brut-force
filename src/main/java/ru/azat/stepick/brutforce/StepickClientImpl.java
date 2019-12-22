package ru.azat.stepick.brutforce;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StepickClientImpl implements StepickClient, StepickBrutForce<String> {
    private final ChromeDriver driver;
    private String login;
    private String password;

    public StepickClientImpl() {
        this.login = null;
        this.password = null;
        System.setProperty("webdriver.chrome.driver","C:\\Users\\naukg\\chromedriver.exe");
        driver = new ChromeDriver();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void waitLoadAttempt() {
        WebDriverWait waitLoad = new WebDriverWait(driver, 100);
        waitLoad.until(ExpectedConditions.elementToBeClickable(By.className("attempt__inner")));
    }

    public String brutForce(String url) {

        driver.get(url);

        waitLoadAttempt();

        List<WebElement> quizElements = getCurrentPageQuizOptions();

        for (int i = 0; i < Math.pow(2, quizElements.size()); i++) {
            if(test(i)) {
                break;
            }
        }

        driver.close();
        return null;
    }

    public boolean test(int iteration) {
        List<WebElement> quizElements = getCurrentPageQuizOptions();

        boolean[] bitArray = longToBitArray(quizElements.size(), iteration);
        for(int i = 0; i < bitArray.length; i++) {
            if(bitArray[i]) {
                WebElement currentElement = quizElements.get(i);
                WebElement parent = currentElement.findElement(By.xpath(".."));
                parent.findElement(By.tagName("input")).click();
            }
        }

        driver.findElement(By.className("submit-submission")).click();
        WebDriverWait waitLoad = new WebDriverWait(driver, 100);
        waitLoad.until(ExpectedConditions.elementToBeClickable(By.className("success")));

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return false;
    }

    private List<WebElement> getCurrentPageQuizOptions() {
        List<WebElement> quizElements = driver.findElements(By.className("choice-quiz-show__option"));
        return quizElements.stream()
                .sorted(Comparator.comparing(WebElement::getText))
                .collect(Collectors.toList());
    }

    private boolean[] longToBitArray(int size, long value) {
        final boolean[] ret = new boolean[size];
        for (int i = 0; i < size; i++) {
            ret[size - 1 - i] = (1 << i & value) != 0;
        }
        return ret;
    }
}
