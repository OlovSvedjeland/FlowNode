package org.example;

import java.util.*;

import static javax.swing.UIManager.get;

public class Project {

    // prerequisite -> incoming edges (all tasks that depend on it)
    private final Map<Task, List<Edge>> incomingEdges = new HashMap<>();

    // dependent -> outgoing edges (all prerequisites it depends on)
    private final Map<Task, List<Edge>> outgoingEdges = new HashMap<>();

    private final List<Task> allTasks = new ArrayList<>();

    public void addTask(String description) {
        Task task = new Task(description);
        incomingEdges.put(task, new ArrayList<>());
        outgoingEdges.put(task, new ArrayList<>());
        allTasks.add(task);
    }

    public List<Task> getAllTasks() {
        return allTasks;
    }

    public void removeTask(String description) {
        Task t = getTaskByDescription(description);
        if (t == null) {
            System.out.println("No such task");
            return;
        }
        incomingEdges.remove(t);
        outgoingEdges.remove(t);
        allTasks.remove(t);
    }

    // child depends on parent => edge: child -> parent
    public void addDependency(Task child, Task parent) {
        Edge e = new Edge(child, parent);
        incomingEdges.get(child).add(e);
        outgoingEdges.get(parent).add(e);

        propagateDownComputedStartTimes(parent);
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
        propagateDownComputedStartTimes(child);
    }

    public void changeDependency(Task child, Task parent, Task newParent) {
        removeDependency(child, parent);
        addDependency(child, newParent);
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

    public int requiredComputedStartFromPrerequisite(Task dependent, Task prerequisite) {
        int prereqEnd = prerequisite.getComputedStart() + prerequisite.getRequiredTime();
        return Math.max(dependent.getEarliestStart(), prereqEnd);
    }

    public Task mostDelayingPrerequisite(Task dependent) {
        Task worst = null;
        int maxStart = dependent.getEarliestStart();

        for (Task prereq : prerequisites(dependent)) {
            int candidate = requiredComputedStartFromPrerequisite(dependent, prereq);
            if (candidate > maxStart) {
                maxStart = candidate;
                worst = prereq;
            }
        }
        return worst;
    }

    public void setEarliestStart(Task task, int time) {
        task.setEarliestStart(time);
        propagateDownComputedStartTimes(task);
    }

    public void recomputeAllTasks() {
        for (Task t : allTasks) {
            propagateDownComputedStartTimes(t);
        }
    }

    // Propagate to tasks that depend on 'root'
    public void propagateDownComputedStartTimes(Task root) {
        Queue<Task> queue = new ArrayDeque<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Task t = queue.poll();

            Task worstPrereq = mostDelayingPrerequisite(t);
            int newActual = (worstPrereq == null)
                    ? t.getEarliestStart()
                    : requiredComputedStartFromPrerequisite(t, worstPrereq);

            if (newActual != t.getComputedStart()) {
                t.setComputedStart(newActual);

                // when a task changes, update tasks that DEPEND ON IT
                queue.addAll(dependents(t));
            }
        }
    }

    public Task getTaskByDescription(String description) {
        String needle = description.trim();

        for (Task task : allTasks) {
            if (task.getDescription().trim().equals(needle)) {
                return task;
            }
        }
        return null;
    }

    public int getMinimumCompletionTime(Task from, Task to) {
        return 0;
    }
}