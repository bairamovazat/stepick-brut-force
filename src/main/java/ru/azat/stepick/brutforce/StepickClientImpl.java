package ru.azat.stepick.brutforce;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StepickClientImpl implements StepickClient, StepickBrutForce<List<Answer>> {
    private final RemoteWebDriver driver;
    private String login;
    private String password;
    private WebDriverWait defaultWaitLoad;
    private WebDriverWait longWaitLoad;

    public StepickClientImpl(RemoteWebDriver remoteWebDriver) {
        this.login = null;
        this.password = null;
        driver = remoteWebDriver;
        defaultWaitLoad = new WebDriverWait(driver, 10);
        longWaitLoad = new WebDriverWait(driver, 100);
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public List<Answer> brutForce(String url) {
        driver.get(url);

        longWaitLoad.until(ExpectedConditions.elementToBeClickable(By.className("submit-submission")));

        List<WebElement> quizElements = getCurrentPageQuizOptions();

        List<Answer> result;

        for (int i = 0; i < Math.pow(2, quizElements.size()); i++) {
            if ((result = test(i)) != null) {
                return result;
            }
        }

        driver.close();
        return null;
    }

    public List<Answer> test(int iteration) {
        List<WebElement> quizElements = getCurrentPageQuizOptions();

        Boolean[] bitArray = longToBitArray(quizElements.size(), iteration);
        for (int i = 0; i < bitArray.length; i++) {
            if (bitArray[i]) {
                quizElements.get(i).click();
            }
        }

        longWaitLoad.until(ExpectedConditions.elementToBeClickable(By.className("submit-submission")));

        WebElement element = driver.findElement(By.className("submit-submission"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element);

        driver.findElement(By.className("submit-submission")).sendKeys(Keys.ENTER);
        WebDriverWait waitLoad = new WebDriverWait(driver, 100);
        waitLoad.until(ExpectedConditions.elementToBeClickable(By.className("success")));
        WebElement successOrError = driver.findElement(By.className("success"));
        if (successOrError.getText().toLowerCase().equals("следующий шаг")) {
            List<Answer> answerList = new ArrayList<>();
            for (int i = 0; i < bitArray.length; i++) {
                answerList.add(new Answer(quizElements.get(i).getText(), bitArray[i]));
            }
            return answerList;
        }

        driver.findElement(By.className("success")).sendKeys(Keys.ENTER);

        longWaitLoad.until(ExpectedConditions.elementToBeClickable(By.className("submit-submission")));

        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return null;
    }

    private List<WebElement> getCurrentPageQuizOptions() {
        List<WebElement> quizElements = driver.findElements(By.className("choice-quiz-show__option"));
        return quizElements.stream()
                .sorted(Comparator.comparing(WebElement::getText))
                .collect(Collectors.toList());
    }

    private Boolean[] longToBitArray(int size, long value) {
        final Boolean[] ret = new Boolean[size];
        for (int i = 0; i < size; i++) {
            ret[size - 1 - i] = (1 << i & value) != 0;
        }
        return ret;
    }
}
