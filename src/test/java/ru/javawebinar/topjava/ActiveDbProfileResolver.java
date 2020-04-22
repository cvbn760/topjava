package ru.javawebinar.topjava;

import org.springframework.test.context.ActiveProfilesResolver;

//http://stackoverflow.com/questions/23871255/spring-profiles-simple-example-of-activeprofilesresolver
public class ActiveDbProfileResolver implements ActiveProfilesResolver {

    @Override
    public String[] resolve(Class<?> aClass) {
        switch (aClass.getSimpleName()) {
            case "JdbcUserServiceTest":
            case "JdbcMealServiceTest":
                return new String[]{Profiles.HSQL_DB, Profiles.JDBC};
            case "JpaUserServiceTest":
            case "JpaMealServiceTest":
                return new String[]{Profiles.HSQL_DB, Profiles.JPA};
            case "DataJpaUserServiceTest":
            case "DataJpaMealServiceTest":
                return new String[]{Profiles.HSQL_DB, Profiles.DATAJPA};
            default:
                return null;
        }
    }
}