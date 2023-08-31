package com.fs.antitheftsdk.network.network;

public interface NetworkConstants {
    int CACHE_SIZE = 10 * 1024 * 1024; // 10 MB
    int DEFAULT_INITIAL_DELAY = 50;
    int DEFAULT_NUM_RETRIES = 2;
    int DEFAULT_TIME_OUT = 10;

    interface ErrorCode {
        String SOME_ERROR_OCCURED = "SOME_ERROR_OCCURED";
        String PARSING_ERROR = "PARSE_ERROR";
        String INTERNAL_ERROR = "INTERNAL_ERROR";
    }

}
