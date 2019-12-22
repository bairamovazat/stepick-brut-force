package ru.azat.stepick.brutforce;

public class Main {
    public static void main(String[] args) {
        StepickBrutForce<String> stepickBrutForce = new StepickClientImpl();
        stepickBrutForce.brutForce("https://stepik.org/lesson/53365/step/7?unit=31457");
    }
}
