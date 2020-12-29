package se.ifmo.pepe.cwdb.backend.util;

import se.ifmo.pepe.cwdb.backend.customtype.DrugsGroup;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

public class DrugsGroupEnumConverter implements AttributeConverter<DrugsGroup, String> {

    @Override
    public String convertToDatabaseColumn(DrugsGroup category) {
        if (category == null) {
            return null;
        }
        return category.getCode();
    }

    @Override
    public DrugsGroup convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(DrugsGroup.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
