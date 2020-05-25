package com.dyi.lambda_test;

public class Function {

    public static boolean retryFun(Retry retry,int retryTime){
        return retry.conductTimesRetry(retryTime);
    }
}
