package com.comix.Commands;

import java.util.LinkedList;

public class MementoStack {
    private static MementoStack instance;
    private final LinkedList<ComicMemento> mementos;

    private MementoStack() {
        mementos = new LinkedList<>();
    }

    public synchronized static MementoStack instance() {
        if (instance == null) {
            instance = new MementoStack();
        }
        return instance;
    }

    public void add(ComicMemento value) {
        mementos.add(value);
    }

    public ComicMemento removeLast() {
        return mementos.removeLast();
    }


    public void clear() {
        mementos.clear();
    }
}
