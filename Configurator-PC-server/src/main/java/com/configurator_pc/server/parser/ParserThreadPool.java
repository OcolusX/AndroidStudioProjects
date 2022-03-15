package com.configurator_pc.server.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.concurrent.*;

public class ParserThreadPool {

    private static final ExecutorService connectionPoolExecutor = Executors.newSingleThreadExecutor();
    private static final ExecutorService parsingPoolExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2);

    public static Connection connect(String url) {
        try {
            return connectionPoolExecutor.submit(new ConnectionTask(url)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void parse(ComponentParsingTask componentParsingTask) {
        parsingPoolExecutor.submit(componentParsingTask);
    }

    private static final class ConnectionTask implements Callable<Connection> {

        private final String url;               // Ссылка для подключения к сайту
        private static final int delay = 100;   // Задержка при подключении

        public ConnectionTask(String url) {
            this.url = url;
        }

        @Override
        public Connection call() throws InterruptedException {
            Thread.sleep(delay);
            return Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        }
    }
}

