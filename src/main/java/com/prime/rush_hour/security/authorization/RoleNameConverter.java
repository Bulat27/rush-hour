package com.prime.rush_hour.security.authorization;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleNameConverter implements AttributeConverter<ApplicationUserRole, String> {

    @Override
    public String convertToDatabaseColumn(ApplicationUserRole applicationUserRole) {
        if(applicationUserRole == null) return null;
        return applicationUserRole.name();
    }

    @Override
    public ApplicationUserRole convertToEntityAttribute(String name) {
        if(name ==  null) return null;
        return ApplicationUserRole.valueOf(name);
    }
}
