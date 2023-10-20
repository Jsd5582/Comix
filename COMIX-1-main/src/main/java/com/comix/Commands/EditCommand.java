package com.comix.Commands;

import com.comix.PersonalCollection.PersonalCollection;
import com.comix.User.User;
import com.comix.model.Comic;

import java.io.IOException;
import java.sql.SQLException;

public class EditCommand implements Command {
    private User user;
    private Comic comic;

    @Override
    public void execute() throws SQLException, IOException {
        PersonalCollection.editComic(comic, user.getUsername());
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
        PersonalCollection.editComic(restored, user.getUsername());
    }

}
