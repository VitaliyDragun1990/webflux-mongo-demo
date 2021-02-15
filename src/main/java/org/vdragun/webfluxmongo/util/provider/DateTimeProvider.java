package org.vdragun.webfluxmongo.util.provider;

import java.time.LocalDateTime;

public interface DateTimeProvider {

    LocalDateTime currentDateTime();
}
