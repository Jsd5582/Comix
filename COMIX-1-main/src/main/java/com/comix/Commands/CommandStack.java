package com.comix.Commands;

import java.util.LinkedList;

public class CommandStack {
    private static CommandStack instance;
    private final LinkedList<Command> commands;
    private final LinkedList<Command> redoList;

    private CommandStack() {
        commands = new LinkedList<>();
        redoList = new LinkedList<>();
    }

    public synchronized static CommandStack instance() {
        if (instance == null) {
            instance = new CommandStack();
        }
        return instance;
    }

    public void add(Command value) {
        commands.add(value);
    }

    public Command removeLast() {
        Command comm = commands.removeLast();
        redoList.add(comm);
        return comm;
    }

    public Command restore() {
        Command comm = redoList.removeLast();
        commands.add(comm);
        return comm;
    }

    public void clearRestore() {
        redoList.clear();
    }

    public void clearAll() {
        commands.clear();
        redoList.clear();
    }
}
