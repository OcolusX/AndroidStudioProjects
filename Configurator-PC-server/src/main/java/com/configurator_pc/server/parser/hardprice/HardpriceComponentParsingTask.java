package com.configurator_pc.server.parser.hardprice;

import com.configurator_pc.server.model.*;
import com.configurator_pc.server.parser.ComponentParsingTask;
import com.configurator_pc.server.parser.ParserThreadPool;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Date;

public final class HardpriceComponentParsingTask extends ComponentParsingTask {

    public HardpriceComponentParsingTask(String url, int productId, int componentTypeId) {
        super(url, productId, componentTypeId);
    }

    @Override
    protected void parseComponent() {
        try {
            Document document = ParserThreadPool.connect(this.url).get();

            Elements elements = document.select("body > div:nth-child(1) > div > div:nth-child(2) > div.row.product-row > div.col-md-8.col-sm-12");
            this.component.setName(elements.select("> h1").text());
            elements = elements.select("> table > tbody > tr");
            for (Element element : elements) {
                Elements eChildren = element.children();
                if (!eChildren.get(1).child(0).className().equals("text-danger date")) {
                    element = eChildren.get(0).select("> b > a").get(0);
                    this.stores.add(new Store(element.text()));
                    String url = "https://hardprice.ru/" + element.attr("href");
                    this.componentOfStores.add(new ComponentOfStore(url));
                    float price = Float.parseFloat(eChildren.get(2).child(0).text().replaceAll("[^0-9]", ""));
                    this.prices.add(new Price(price, 1, new Date(System.currentTimeMillis())));
                }
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


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
