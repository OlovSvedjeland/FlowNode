package org.example;

import java.util.*;

public class Project {

    // prerequisite -> incoming edges (all tasks that depend on it)
    private final Map<Task, List<Edge>> incomingEdges = new HashMap<>();

    // dependent -> outgoing edges (all prerequisites it depends on)
    private final Map<Task, List<Edge>> outgoingEdges = new HashMap<>();

    public void addTask(String description) {
        Task task = new Task(description);
        incomingEdges.put(task, new ArrayList<>());
        outgoingEdges.put(task, new ArrayList<>());
    }

    // child depends on parent => edge: child -> parent
    public void addDependency(Task child, Task parent) {
        Edge e = new Edge(child, parent); // from=dependent, to=prerequisite

        if (incomingEdges.containsKey(child)) {
            incomingEdges.get(child).add(e);
        } else incomingEdges.put(child, new ArrayList<>());
        if (outgoingEdges.containsKey(parent)) {
            outgoingEdges.get(parent).add(e);
        } else outgoingEdges.put(parent, new ArrayList<>());

        propagateDownActualStartTimes(parent);
    }

    public Edge getEdge(Task child, Task parent) {
        List<Edge> outEdges = this.outgoingEdges.get(child);
        for (Edge e : outEdges) {
            if (e.getParent().equals(parent)) {
                return e;
            }
        }
        return null;
    }

    public void removeDependency(Task child, Task parent) {
        Edge e = getEdge(child, parent);
        if (e != null) {
            if (incomingEdges.containsKey(child)) {
                incomingEdges.get(child).remove(e);
            }
            if (outgoingEdges.containsKey(parent)) {
                outgoingEdges.get(parent).remove(e);
            }
        }
        propagateDownActualStartTimes(child);
    }

    public void changeDependency(Task child, Task parent, Task newParent) {
        removeDependency(child, parent);
        addDependency(child, newParent);
        propagateDownActualStartTimes(child);
    }

    public List<Edge> incoming(Task prerequisite) {
        return incomingEdges.getOrDefault(prerequisite, List.of());
    }

    public List<Edge> outgoing(Task dependent) {
        return outgoingEdges.getOrDefault(dependent, List.of());
    }

    /** Tasks that must finish before 'task' can start */
    public List<Task> prerequisites(Task task) {
        return outgoing(task).stream()
                .map(Edge::getParent) // <- rename this in Edge ideally (see note)
                .distinct()
                .toList();
    }

    // Tasks that are blocked by 'task' (i.e., that depend on it)
    public List<Task> dependents(Task task) {
        return incoming(task).stream()
                .map(Edge::getChild) // <- rename this in Edge ideally
                .distinct()
                .toList();
    }

    // ----- Scheduling bits -----

    public int requiredActualStartFromPrerequisite(Task dependent, Task prerequisite) {
        int prereqEnd = prerequisite.getActualStartTime() + prerequisite.getRequiredTime();
        return Math.max(dependent.getBaseLineStart(), prereqEnd);
    }

    public Task mostDelayingPrerequisite(Task dependent) {
        Task worst = null;
        int maxStart = dependent.getBaseLineStart();

        for (Task prereq : prerequisites(dependent)) {
            int candidate = requiredActualStartFromPrerequisite(dependent, prereq);
            if (candidate > maxStart) {
                maxStart = candidate;
                worst = prereq;
            }
        }
        return worst;
    }

    public void setNewBaseLineStart(Task task, int time) {
        task.setBaseLineStart(time);
        propagateDownActualStartTimes(task);
    }

    // Propagate to tasks that depend on 'root'
    public void propagateDownActualStartTimes(Task root) {
        Queue<Task> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Task t = queue.poll();

            Task worstPrereq = mostDelayingPrerequisite(t);
            int newActual = (worstPrereq == null)
                    ? t.getBaseLineStart()
                    : requiredActualStartFromPrerequisite(t, worstPrereq);

            if (newActual != t.getActualStartTime()) {
                t.setActualStartTime(newActual);

                // when a task changes, update tasks that DEPEND ON IT
                queue.addAll(dependents(t));
            }
        }
    }
}