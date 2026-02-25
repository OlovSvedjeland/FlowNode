package org.example;

public class Task {

    String description;

    int baseLineStart;
    int actualStartTime;
    int requiredTime;
    int duration;

    public Task(String description) {
        this.description = description;
        this.baseLineStart = 0;
        this.requiredTime = 0;
        this.actualStartTime = 0;
    }

    public int getBaseLineStart() {
        return baseLineStart;
    }
    public void setBaseLineStart(int baseLineStart) {
        this.baseLineStart = Math.max(0, baseLineStart);
    }

    public int getRequiredTime() {
        return requiredTime;
    }
    public void setRequiredTime(int requiredTime) {
        this.requiredTime = Math.max(0, requiredTime);
    }

    public void setActualStartTime(int actualStartTime) {
        this.actualStartTime = Math.max(baseLineStart, actualStartTime);
    }

    public int getActualStartTime() {
        return actualStartTime;
    }

}
