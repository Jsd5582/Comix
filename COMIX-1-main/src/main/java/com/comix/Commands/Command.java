package com.comix.Commands;

import com.comix.User.User;
import com.comix.model.Comic;

import java.io.IOException;
import java.sql.SQLException;

public interface Command {
    void execute() throws SQLException, IOException;
    void setUser(User user);
    void setComic(Comic comic);
    void undo() throws SQLException, IOException;
}
