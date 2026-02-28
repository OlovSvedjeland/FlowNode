package org.example;

public class Task {

    String description;

    int earliestStart;
    int computedStart;
    int requiredTime;


    public Task(String description) {
        this.description = description;
        this.earliestStart = 0;
        this.requiredTime = 0;
        this.computedStart = 0;
    }

    public String getDescription() {
        return description;
    }

    public int getEarliestStart() {
        return earliestStart;
    }
    public void setEarliestStart(int earliestStart) {
        this.earliestStart = Math.max(0, earliestStart);
    }

    public int getRequiredTime() {
        return requiredTime;
    }
    public void setRequiredTime(int requiredTime) {
        this.requiredTime = Math.max(0, requiredTime);
    }

    public void setComputedStart(int computedStart) {
        this.computedStart = Math.max(earliestStart, computedStart);
    }

    public int getComputedStart() {
        return computedStart;
    }

}
