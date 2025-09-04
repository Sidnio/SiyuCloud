package com.sidnio.siyucloud.utils.error;

import androidx.annotation.NonNull;

public abstract class ErrorCallback {

    public abstract void onError(@NonNull String tag, @NonNull Throwable error);
}
