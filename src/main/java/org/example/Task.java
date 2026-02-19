package org.example;

public class Task {

    String description;

    int baseStartTime;
    int actualStartTime;
    int requiredTime;
    int duration;

    // phase out these
    int actualEndTime;
    int baseEndTime;

    public Task(String description) {
        this.description = description;
        this.baseStartTime = 0;
        this.requiredTime = 0;
        this.actualStartTime = 0;
    }

    public int getBaseStartTime() {
        return baseStartTime;
    }
    public void setBaseStartTime(int baseStartTime) {
        this.baseStartTime = Math.max(0, baseStartTime);
    }

    public int getRequiredTime() {
        return Math.max(0, requiredTime);
    }
    public void setRequiredTime(int requiredTime) {
        this.requiredTime = Math.max(0, requiredTime);
    }
    public int getDuration() {
        return duration;
    }

    public void setActualStartTime(int actualStartTime) {
        this.actualStartTime = Math.max(baseStartTime, actualStartTime);
    }
    public int getActualStartTime() {
        return actualStartTime;
    }

    public int getEndtime() {
        return actualStartTime + duration;
    }

    public int getEffectiveness() {
        return duration / requiredTime;
    }
}
