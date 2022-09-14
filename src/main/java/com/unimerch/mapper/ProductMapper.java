package com.unimerch.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimerch.repository.model.Product;

import java.io.IOException;
import java.math.BigDecimal;


public class ProductMapper extends StdDeserializer<Product> {

    public ProductMapper() {
        this(null);
    }

    public ProductMapper(Class<?> vc) {
        super(vc);
    }

    @Override
    public Product deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode productNode = jsonParser.getCodec().readTree(jsonParser);
        String asin = productNode.get("asin").textValue();
        BigDecimal price = BigDecimal.valueOf(productNode.get("price").asDouble());
        String priceHtml = productNode.get("priceHtml").textValue();
        return new Product(asin, price, priceHtml);
    }

}
