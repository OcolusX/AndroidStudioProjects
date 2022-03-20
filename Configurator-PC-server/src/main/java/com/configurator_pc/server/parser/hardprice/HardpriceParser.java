package com.configurator_pc.server.parser.hardprice;

import com.configurator_pc.server.parser.ComponentParsingTask;
import com.configurator_pc.server.parser.Parser;
import com.configurator_pc.server.parser.ParserThreadPool;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public final class HardpriceParser extends Parser {

    private static final String componentTypesJSON = "componentTypes/hardprice.json";

    public HardpriceParser() {
        super(componentTypesJSON);
    }

    @Override
    protected void parseList(int componentTypeId, String url) {
        try {
            Document document = ParserThreadPool.connect(url).get();

            Elements elements = document.select("body > div:nth-child(1) > div > div > div.products-list-v2.products-list > div");
            for(Element element : elements) {
                String href = element.select("> div.products-list-v2__item-head > div > a").attr("href");
                ComponentParsingTask parsingTask = new HardpriceComponentParsingTask("https://hardprice.ru/" + href, componentTypeId);
                ParserThreadPool.parse(parsingTask);
            }

            elements = document.select("body > div:nth-child(1) > div > div > ul > li");
            if(elements.size() != 0) {
                Element element = elements.get(elements.size() - 1).child(0);
                if (element.text().equals("→")) {
                    parseList(componentTypeId, "https://hardprice.ru" +  element.attr("href"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
