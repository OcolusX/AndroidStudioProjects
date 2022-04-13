package com.example.configurator_pc.repository;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.MutableLiveData;

import com.example.configurator_pc.model.Attribute;
import com.example.configurator_pc.model.Component;
import com.example.configurator_pc.model.ComponentType;
import com.example.configurator_pc.model.Configuration;
import com.example.configurator_pc.model.Currency;
import com.example.configurator_pc.model.Price;
import com.example.configurator_pc.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Repository {

    private final HttpConnection httpConnection;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final String url = "https://ee8f-185-81-66-207.ngrok.io";
    private static final String THREAD_NAME = "ServerConnectionThread";
    private Thread connectionThread;

    public Repository(Context context) {
        httpConnection = new HttpConnection(context);
    }

    public void cancel() {
        if (connectionThread != null && connectionThread.isAlive()) {
            connectionThread.interrupt();
        }
    }


    // Возвращает список конфигураций (сборок) из БД, принадлежащих конкретному пользователю
    public List<Configuration> getConfigurationList(User user) {
        // TODO : должен возвращать список конфигураций(сборок) для конкретного пользователя

        return null;
    }

    // Возвращает список компонентов конкретного типа из БД,
    // начиная с номера fromIndex и заканчивая номером toIndex;
    // Например, вызов getComponentList(ComponentType.CPU, 0, 49) вернёт первые 50 процессоров из БД
    public MutableLiveData<List<Component>> loadComponentList(ComponentType type, int fromIndex, int toIndex) {
        MutableLiveData<List<Component>> liveData = new MutableLiveData<>();
        connectionThread = new Thread(() -> {
            try {
                // Получаем ответ в виде json-строки и парсим ответ в JSONArray
                String jsonComponentString = null;
                while (jsonComponentString == null) {
                    jsonComponentString = httpConnection.connect(url + "/components/" + type.getId()
                            + "?from_index=" + fromIndex + "&to_index=" + toIndex);
                }
                JSONArray jsonComponentList = new JSONArray(jsonComponentString);
                List<Component> componentList = new LinkedList<>();

                // Проходимся по каждому JSONObject, формируем объект Component и добавляем его в список
                for (int i = 0; i < jsonComponentList.length(); i++) {
                    JSONObject jsonComponent = jsonComponentList.getJSONObject(i);

                    // Формируем список аттрибутов
                    JSONObject attributes = jsonComponent.getJSONObject("attributes");
                    List<Attribute> attributeList = new LinkedList<>();
                    Iterator<String> keys = attributes.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        attributeList.add(new Attribute(next, attributes.getString(next)));
                    }

                    // Формируем список цен
                    JSONArray prices = jsonComponent.getJSONArray("prices");
                    List<Price> priceList = new LinkedList<>();
                    for (int j = 0; j < prices.length(); j++) {
                        JSONObject jsonPrice = prices.getJSONObject(j);
                        priceList.add(new Price(
                                Float.parseFloat(jsonPrice.getString("price")),
                                Currency.valueOf(jsonPrice.getString("currency")),
                                jsonPrice.getString("store"),
                                jsonPrice.getString("url"),
                                dateFormat.parse(jsonPrice.getString("date"))
                        ));
                    }

                    // Формируем объект Component
                    componentList.add(new Component(
                            jsonComponent.getString("name"),
                            type,
                            attributeList,
                            priceList
                    ));
                }
                liveData.postValue(componentList);
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        });
        connectionThread.setName(THREAD_NAME);
        connectionThread.start();
        return liveData;
    }

    // Возвращает общее количество компонентов конкретного типа из БД
    public int getComponentsSize(ComponentType type) {
        String size = httpConnection.connect(url + "/components/" + type.getId());
        return size == null ? -1 : Integer.parseInt(size);
    }


    private static class HttpConnection {

        private final Context context;

        public HttpConnection(Context context) {
            this.context = context;
        }

        public String connect(String url) {
            try {

                if (checkInternetConnection()) {
                    Scanner s = new Scanner(openHttpConnection(url)).useDelimiter("\\A");
                    return s.hasNext() ? s.next() : "";
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
            return null;
        }

        private InputStream openHttpConnection(String urlString) {

            InputStream inputStream = null;
            int responseCode = -1;

            try {
                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();

                if (!(urlConnection instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an http URL");
                }

                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setAllowUserInteraction(false);
                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                responseCode = httpURLConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return inputStream;
        }

        // TODO : добавить вывод всплывающего сообщения, если нет интернет подклбчения
        private boolean checkInternetConnection() {
            // get Connectivity Manager object to check connection
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

            // Check for network connections
            if (connectivityManager.getNetworkInfo(0).getState() ==
                    android.net.NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(0).getState() ==
                            android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() ==
                            android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                return true;
            } else if (
                    connectivityManager.getNetworkInfo(0).getState() ==
                            android.net.NetworkInfo.State.DISCONNECTED ||
                            connectivityManager.getNetworkInfo(1).getState() ==
                                    android.net.NetworkInfo.State.DISCONNECTED) {
                return false;
            }
            return false;
        }
    }
}
