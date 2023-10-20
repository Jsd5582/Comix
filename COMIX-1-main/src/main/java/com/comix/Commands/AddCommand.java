package com.comix.Commands;

import com.comix.PersonalCollection.PersonalCollection;
import com.comix.User.User;
import com.comix.model.Comic;

import java.io.IOException;
import java.sql.SQLException;

public class AddCommand implements Command {
    private User user;
    private Comic comic;

    @Override
    public void execute() {
        PersonalCollection.addComic(comic, user.getUsername());
        ComicMemento memento = comic.createMemento();
        MementoStack stack = MementoStack.instance();
        stack.add(memento);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setComic(Comic comic) {
        this.comic = comic;
    }

    @Override
    public void undo() throws SQLException, IOException {
        ComicMemento memento = MementoStack.instance().removeLast();
        comic.setMemento(memento);
        PersonalCollection.remove(user.getUsername(), comic);
    }
}
