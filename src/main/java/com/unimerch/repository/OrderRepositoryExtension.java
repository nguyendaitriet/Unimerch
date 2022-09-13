package com.unimerch.repository;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class OrderRepositoryExtension {
    @PersistenceContext
    private EntityManager entityManager;


    public void deleteAllByDate(List<String> dateStrings) {
        StringBuilder query = new StringBuilder("DELETE FROM orders WHERE ");

        int size = dateStrings.size();
        for (int i = 0; i < size; i++) {
            String dateString = dateStrings.get(i);
            String format = String.format("CAST(date AS DATE) = %s", dateString);
            query.append(String.format(format, dateString))
                    .append(i < size - 1 ? " OR " : "");
        }
        entityManager.createNativeQuery(query.toString()).executeUpdate();
    }
}
