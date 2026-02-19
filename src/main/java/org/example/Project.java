package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project {

    List<Edge> edges;
    private final HashMap<Task, List<Edge>> incomingEdges;
    private final HashMap<Task, List<Edge>> outgoingEdges;

    public Project() {
        this.incomingEdges = new HashMap<>();
        this.outgoingEdges = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public void addTask(String description) {
        Task task = new Task(description);
        incomingEdges.put(task, new ArrayList<>());
        outgoingEdges.put(task, new ArrayList<>());
    }

    public List<Edge> incoming(Task t) { return incomingEdges.getOrDefault(t, List.of()); }
    public List<Edge> outgoing(Task t) { return outgoingEdges.getOrDefault(t, List.of()); }

    public List<Task> parents(Task t) {
        return incoming(t).stream().map(Edge::getStart).distinct().toList(); }

    public List<Task> children(Task t) {
        return outgoing(t).stream().map(Edge::getEnd).distinct().toList();
    }

    public void setRequiredTime(Task t, long duration) {
        // iterate down through children
    }

    public void setStartTime(Task t) {
        // iterate down through children
    }

    public int getDurationFromParent(Task t, Edge e) {
        DependencyType type = e.getDependencyType();
        int childEndTime = t.getActualStartTime() + t.getRequiredTime();
        Task parent = e.getEnd();
        int parentEndTime = parent.getActualStartTime() + parent.getDuration(); // behöver getDuration va en variabel inuti task ?
        int endDifference = parentEndTime - childEndTime;

        return switch (type) {
            case FINISH_START -> Math.max(t.getActualStartTime() + t.requiredTime + endDifference, t.getActualStartTime() + t.requiredTime);
            case FINISH_FINISH -> Math.
        }
    }

    public int startTimeFromParent(Task t, Edge e) {
        DependencyType type = e.getDependencyType();
        Task parent = e.getStart();

        return switch (type) {
            case START_START -> Math.max(t.getBaseStartTime(), parent.getActualStartTime());
            case START_FINISH -> Math.min(t.getBaseStartTime(), parent.getActualStartTime() + parent.duration);
            default -> 0;
        };
    }
}
