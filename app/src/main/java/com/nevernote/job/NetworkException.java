package com.nevernote.job;

/**
 * Created by ali on 28.02.18.
 */

public class NetworkException extends RuntimeException {

    private final int mErrorCode;

    public NetworkException(int errorCode) {
        mErrorCode = errorCode;
    }

    public boolean shouldRetry() {
        return mErrorCode < 400 || mErrorCode > 499;
    }

}