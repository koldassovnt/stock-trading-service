package kz.nkoldassov.stocktrading.validator;

import io.javalin.http.Context;
import io.javalin.http.Handler;

public class ValidatorMiddleware implements Handler {

    private final Class<?> dtoClass;

    public ValidatorMiddleware(Class<?> dtoClass) {
        this.dtoClass = dtoClass;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Object dto = ctx.bodyAsClass(dtoClass);
        AnnotationValidator.validate(dto);
        ctx.attribute("validatedDto", dto);
    }

}