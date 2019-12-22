package ru.azat.stepick.brutforce;

import org.openqa.selenium.chrome.ChromeDriver;

import java.io.Reader;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите урл для задания из степика: ");
        String url = scanner.nextLine();
        String driverUrl = "C:\\Users\\Azat\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverUrl);
        ChromeDriver driver = new ChromeDriver();
        StepickBrutForce<List<Answer>> stepickBrutForce = new StepickClientImpl(driver);
        System.out.println();
        List<Answer> result = stepickBrutForce.brutForce(url);
        System.out.println(result);
    }
}
