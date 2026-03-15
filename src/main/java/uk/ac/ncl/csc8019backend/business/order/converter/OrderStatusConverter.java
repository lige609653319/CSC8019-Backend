package uk.ac.ncl.csc8019backend.business.order.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderStatus;

// OrderStatusConverter.java
@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public OrderStatus convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : OrderStatus.fromValue(dbData);
    }
}
