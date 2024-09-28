package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class UserInputHandler {
    private final BufferedReader reader;
    private final PrintStream out;

    public UserInputHandler(InputStream in, PrintStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.out = out;
    }

    public String getUserInput() throws IOException {
        return reader.readLine();
    }

    public void displayMessage(String message) {
        out.println(message);
    }
}
