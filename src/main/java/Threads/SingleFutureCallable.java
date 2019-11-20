/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Threads;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 *
 * @author Dennis
 */
public class SingleFutureCallable {
    
    private final ExecutorService exService = Executors.newCachedThreadPool();
    
    public String run(String url) throws InterruptedException, ExecutionException{
        
        final Future<String> callFuture = exService.submit(new SingleFutureCallable().new CallableThread(url));
        final String callVal = callFuture.get();
        exService.shutdown();
        return callVal;
        
    }
    
    
    
    class CallableThread implements Callable<String>{
        
        private String urlString;
        
        public CallableThread(String url){
            this.urlString = url;
        }
        
        @Override
        public String call() throws Exception {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json;charset=UTF-8");
            conn.setRequestProperty("User-Agent", "server");
            Scanner scan = new Scanner(conn.getInputStream());
            String response = null;
            if(scan.hasNext()){
                response = scan.nextLine();
            }
            Gson gson = new Gson();
            String result = gson.fromJson(response, JsonObject.class).toString();
            scan.close();
            return result;
        }
        
    }
    
}
