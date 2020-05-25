package com.dyi.lambda_test;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.assertFalse;

public class FunctionTest {
    private static AtomicBoolean isGetSuccess = new AtomicBoolean();
    @Test
    public void testRetryFun(){
        assertFalse(Function.retryFun(
                (int retryTimes)-> {
                    while (retryTimes-- > 0) {
                        System.out.println("循环了"+retryTimes);
                        isGetSuccess.compareAndSet(false, false);
                        String url = "http://wwww.baidu.com";
                        OkHttpClient okHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder()
                                .url(url)
                                .get()
                                .build();
                        Call call = okHttpClient.newCall(request);

                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                if(isGetSuccess.compareAndSet(true,false)){
                                    return;
                                }
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (isGetSuccess.compareAndSet(false, true)) {
                                    return;
                                }

                            }
                        });
                        if(isGetSuccess.compareAndSet(true,false)){
                            break;
                        }
                    }
                    return retryTimes == -1 ? false : true;
                },5));
    }
}
