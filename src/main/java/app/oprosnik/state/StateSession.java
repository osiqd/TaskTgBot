package app.oprosnik.state;

import app.oprosnik.Question;

import java.io.FileNotFoundException;

public interface StateSession {
    enum State {INIT, ACTION, CHECK, END, ERROR}

    public Question action() throws FileNotFoundException;

    public boolean check(String answer);

    public String end();

    public State getState();

}
