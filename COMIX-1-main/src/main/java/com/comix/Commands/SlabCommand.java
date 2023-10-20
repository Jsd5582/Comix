package com.comix.Commands;

import com.comix.PersonalCollection.PersonalCollection;
import com.comix.User.User;
import com.comix.model.Comic;

import java.io.IOException;
import java.sql.SQLException;

public class SlabCommand implements Command {
    private User user;
    private Comic comic;

    @Override
    public void execute() throws SQLException, IOException {
        ComicMemento memento = comic.createMemento();
        MementoStack.instance().add(memento);
        PersonalCollection.slabComic(comic, user.getUsername());
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
        Comic restored = new Comic(memento);
        PersonalCollection.slabComic(restored, user.getUsername());
    }
}
