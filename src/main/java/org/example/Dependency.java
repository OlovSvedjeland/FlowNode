package org.example;

public interface Dependency {

    // later, edges will implement these such that start-delays or finish-delays will be a product of preRequisits
    public int startlag(Task start, Task end);
    public int endLag(Task start, Task end);


}