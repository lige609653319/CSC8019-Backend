package uk.ac.ncl.csc8019backend.business.order.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import uk.ac.ncl.csc8019backend.business.order.enums.OrderType;

@Converter(autoApply = true)
public class OrderTypeConverter implements AttributeConverter<OrderType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderType attribute) {
        return attribute == null ? null : attribute.getValue();
    }
    @Override
    public OrderType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : OrderType.fromValue(dbData);
    }
}
