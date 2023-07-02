package ru.otus.filinovich.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.filinovich.config.LocaleProvider;

@Component
@RequiredArgsConstructor
public class MessageProvider {

    private final MessageSource source;

    private final LocaleProvider localeProvider;

    public String getMessage(String placeholder) {
        return source.getMessage(placeholder, new String[]{}, localeProvider.getLocale());
    }
}
