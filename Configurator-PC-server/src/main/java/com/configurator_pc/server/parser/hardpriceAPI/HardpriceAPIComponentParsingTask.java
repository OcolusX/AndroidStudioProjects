package com.configurator_pc.server.parser.hardpriceAPI;

import com.configurator_pc.server.model.*;
import com.configurator_pc.server.parser.ComponentParsingTask;
import com.configurator_pc.server.parser.ParserThreadPool;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Date;

public class HardpriceAPIComponentParsingTask extends ComponentParsingTask {

    private final String url;

    public HardpriceAPIComponentParsingTask(String url, int componentTypeId, String vendor, String image, String description) {
        super(componentTypeId);
        this.url = url;
        this.attributeTypes.add(new AttributeType("Производитель"));
        this.componentAttributes.add(new ComponentAttribute(vendor));
        this.component.setImage(image);
        this.component.setDescription(description);
    }

    @Override
    protected void parseComponent() {
        try {
            Document document = ParserThreadPool.connect(url).ignoreContentType(true).get();

            Elements elements = document.select("body > div:nth-child(1) > div > div:nth-child(2) > div.row.product-row > div.col-md-8.col-sm-12");
            this.component.setName(elements.select("> h1").text());
            elements = elements.select("> table > tbody > tr");
            for (Element element : elements) {
                Elements eChildren = element.children();
                element = eChildren.get(0).select("> b > a").get(0);
                this.stores.add(new Store(element.text()));
                String url = "https://hardprice.ru" + element.attr("href");
                this.componentOfStores.add(new ComponentOfStore(url));
                String strPrice = eChildren.get(2).child(0).text().replaceAll("[^0-9]", "");
                float price = strPrice.isEmpty() ? -1 : Float.parseFloat(strPrice);
                this.prices.add(new Price(price, 1, new Date(System.currentTimeMillis())));
            }
            elements = document.select("#productSpecs > table > tbody > tr");
            int size = elements.size();
            Element element = elements.get(2);
            for (int i = 2; i < size; i++) {
                elements = element.children();
                this.attributeTypes.add(new AttributeType(elements.get(0).text()));
                this.componentAttributes.add(new ComponentAttribute(elements.get(1).text()));

                element = element.nextElementSibling();
            }

        } catch (ConnectException e) {
            e.printStackTrace();
            parseComponent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
