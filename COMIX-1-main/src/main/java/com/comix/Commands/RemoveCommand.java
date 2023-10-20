package com.comix.Commands;

import com.comix.PersonalCollection.PersonalCollection;
import com.comix.User.User;
import com.comix.model.Comic;

import java.io.IOException;
import java.sql.SQLException;

public class RemoveCommand implements Command {
    private Comic comic;
    private User user;

    @Override
    public void execute() {
        ComicMemento memento = comic.createMemento();
        MementoStack.instance().add(memento);
        PersonalCollection.remove(user.getUsername(), comic);
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
        PersonalCollection.addComic(comic, user.getUsername());
    }
}
