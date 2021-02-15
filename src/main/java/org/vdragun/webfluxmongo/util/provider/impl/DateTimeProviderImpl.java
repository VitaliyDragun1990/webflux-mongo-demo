package org.vdragun.webfluxmongo.util.provider.impl;

import org.springframework.stereotype.Component;
import org.vdragun.webfluxmongo.util.provider.DateTimeProvider;

import java.time.LocalDateTime;

@Component
public class DateTimeProviderImpl implements DateTimeProvider {

    @Override
    public LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }
}
