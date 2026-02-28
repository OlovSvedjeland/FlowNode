package org.example;

public interface Dependency {

    // TODO : since edges represents relationships between tasks, lags in start or end time is a property of an edge
    public int startlag(Task start, Task end);
    public int endLag(Task start, Task end);


}