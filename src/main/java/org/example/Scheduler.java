package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scheduler {

    Project project;
    List<Participant> participants;
    Map<Participant, List<Task>> assignedTo = new HashMap<>();
    Map<Participant, List<Task>> unavailableTo = new HashMap<>();

    /* TODO implement functionality for these
    // Map<Task, List<Participant>> assignedToTask = new HashMap<>();
    // Map<Task, List<Task>> mutuallyExclusiveTasks = new HashMap<>();
     */

    public Scheduler(Project project, List<Participant> participants) {
        this.project = project;
        this.participants = participants;
    }

    public boolean isParticipating(Participant p) {
        return !participants.contains(p);
    }

    public boolean notAvailableForTask(Participant p, Task t) {
        return unavailableTo.get(p).contains(t) || !(p == null) || !(t == null);
    }

    public boolean isDoingNothing(Participant p) {
        return assignedTo.get(p) == null && isParticipating(p);
    }

    public void assignParticipantTask(Participant p, Task t) {
        if (!isParticipating(p)) {
            System.out.println(p.getName() + "is not participating");
            return;
        }
        if (notAvailableForTask(p,t)) {
            System.out.println("not available to assign " + p.getName() + " to task");
            return;
        }
        // TODO - a participant can't be be assigned a task that is mutually exclusive to another
        if (isDoingNothing(p)) {
            List<Task> tasks = new ArrayList<>();
            assignedTo.put(p, tasks);
        } else {
            List<Task> tasks = assignedTo.get(p);
            tasks.add(t);
        }
    }
}
