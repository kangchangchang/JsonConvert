package com.datacenter;

import com.datacenter.convert.JacksonUtils;
import com.datacenter.convert.JsonPathUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author pc
 * @date Create in  2023/2/23
 */
@Slf4j
public class JsonPathUtilsTest {

    private static String json="{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}}";
    public static void main(String[] args) {

        log.info("store.book[0].category==={}",JsonPathUtils.read(json,"$.store.book[0].category").toString());
        log.info("readList==={}",JsonPathUtils.readPathList(json,"$.store.book.*"));

        List<String>  readList=JsonPathUtils.readPathList(json,"$.store.book.*");
        for(String str:readList){
            log.info(JacksonUtils.toJson(JsonPathUtils.read(json,str)));
        }


    }
}
