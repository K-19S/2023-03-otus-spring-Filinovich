package ru.otus.filinovich.service.io.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.filinovich.service.io.IOService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.IOException;


@Service
public class ConsoleIOService implements IOService {

    private final PrintStream printer;

    private final BufferedReader reader;

    public ConsoleIOService(@Value("#{ T(java.lang.System).out}") PrintStream out,
                            @Value("#{ T(java.lang.System).in}") InputStream in) {
        this.printer = out;
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public void outputString(String text) {
        printer.println(text);
    }

    @Override
    public String readString() {
        do {
            try {
                String line = reader.readLine();
                if (!line.isBlank()) {
                    return line;
                }
            } catch (IOException e) {
                outputString("Try again");
            }
        } while(true);
    }

    @Override
    public Long readLong() {
        do {
            try {
                return Long.parseLong(readString());
            } catch (NumberFormatException e) {
                outputString("Try again");
            }
        } while(true);
    }

    @Override
    public Integer readInteger(int min, int max) {
        do {
            Integer num = readLong().intValue();
            if (num >= min && num <= max) {
                return num;
            }
        } while (true);
    }

    @Override
    public String readStringWithPrompt(String promt) {
        outputString(promt);
        return readString();
    }

    @Override
    public Long readLongWithPrompt(String promt) {
        outputString(promt);
        return readLong();
    }
}
