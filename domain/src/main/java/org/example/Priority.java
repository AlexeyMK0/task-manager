package org.example;

public record Priority(int value) {
    public static final Priority Low = new Priority(10);

    public static final Priority Medium = new Priority(20);

    public static final Priority High = new Priority(30);
}
