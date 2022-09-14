package com.unimerch.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimerch.dto.product.ProductData;
import com.unimerch.repository.model.Product;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper extends StdDeserializer<ProductData> {

    public ProductMapper() {
        this(null);
    }

    public ProductMapper(Class<?> vc) {
        super(vc);
    }

    @Override
    public ProductData deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode productNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode productNodeList = productNode.fields().next().getValue();
        List<Product> productList = new ArrayList<>();
        ProductData productData = new ProductData();
        for (JsonNode node : productNodeList) {
            String asin = node.get("asin").textValue();
            BigDecimal price = BigDecimal.valueOf(node.get("price").asDouble());
            String priceHtml = node.get("priceHtml").textValue();
            productList.add(new Product(asin, price, priceHtml));
        }
        productData.setProductList(productList);
        return productData;
    }

}
