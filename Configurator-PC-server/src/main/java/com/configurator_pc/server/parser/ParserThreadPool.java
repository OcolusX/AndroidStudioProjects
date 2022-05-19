package com.configurator_pc.server.parser;

import org.hibernate.annotations.common.util.impl.Log;
import org.hibernate.annotations.common.util.impl.Log_$logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.concurrent.*;

public class ParserThreadPool {

    private static final ExecutorService connectionPoolExecutor = Executors.newSingleThreadExecutor();
    private static final ExecutorService parsingPoolExecutor = Executors.newFixedThreadPool(4);

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

    public static void shutdown() {
        connectionPoolExecutor.shutdown();
        parsingPoolExecutor.shutdown();
    }

    private static final class ConnectionTask implements Callable<Connection> {

        private final String url;                // Ссылка для подключения к сайту
        private static final int delay = 5000;    // Задержка при подключении

        public ConnectionTask(String url) {
            this.url = url;
        }

        @Override
        public Connection call() throws InterruptedException {
            Thread.sleep(delay);
            return Jsoup.connect(url)
                    .cookie("hardprice", "u7q76cdgqch27ote7p4o0n42op")
                    .referrer("https://yandex.ru")
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.174 YaBrowser/22.1.5.810 Yowser/2.5 Safari/537.36");
        }
    }
}

