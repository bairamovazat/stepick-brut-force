package ru.azat.stepick.brutforce;

public class Answer {
    private String value;
    private Boolean isTrue;

    public Answer(String value, Boolean isTrue) {
        this.value = value;
        this.isTrue = isTrue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getTrue() {
        return isTrue;
    }

    public void setTrue(Boolean aTrue) {
        isTrue = aTrue;
    }

    @Override
    public String toString() {
        return "Value: " + getValue() + ", isTrue: " + getTrue() + ";";
    }
}
