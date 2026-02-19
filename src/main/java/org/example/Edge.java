package org.example;

public class Edge {

    DependencyType dependencyType;
    Task start;
    Task end;

    public Edge(Task start, Task end) {
        this.dependencyType = null;
        this.start = start;
        this.end = end;
    }

    public void assignDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public Task getStart() {
        return start;
    }

    public Task getEnd() {
        return end;
    }

    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }

    public DependencyType getDependencyType() {
        return dependencyType;
    }
}

