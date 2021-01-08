package com.project.eniac.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

    private IOUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (ObjectUtils.isEmpty(closeable)) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

}
