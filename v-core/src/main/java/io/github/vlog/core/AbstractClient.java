package io.github.vlog.core;

import io.github.vlog.core.exception.LogQueueConnectException;
import lombok.Data;

@Data
public abstract class AbstractClient {

    private static AbstractClient client;

    public static AbstractClient getClient() {
        return client;
    }

    public static void setClient(AbstractClient abstractClient) {
        client = abstractClient;
    }

    public void pushMessage(String key, String strings) throws LogQueueConnectException {
    }
}
