package org.example;

public class Edge {

    Task parent;
    Task child;
    int startLag;
    int finishLag;

    public Edge(Task child, Task parent) {
        this.parent = child;
        this.child = parent;
    }

    int getStartLag() {
        return startLag;
    }

    void setStartLag(int startLag) {
        this.startLag = startLag;
    }

    int getFinishLag() {
        return finishLag;
    }

    public Task getParent() {
        return parent;
    }

    public Task getChild() {
        return child;
    }


}

