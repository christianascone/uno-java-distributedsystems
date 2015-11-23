package rmiSourceCode.compute;

public interface Task<T> {
    T execute();
}