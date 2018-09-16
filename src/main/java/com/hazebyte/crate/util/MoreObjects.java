package com.hazebyte.crate.util;

import org.jetbrains.annotations.Nullable;

public class MoreObjects {
    public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
        return first != null ? first : second;
    }
}
