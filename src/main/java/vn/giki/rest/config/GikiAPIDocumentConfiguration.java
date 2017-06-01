package vn.giki.rest.config;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SwaggerDefinition(basePath = "/", host = "", info = @Info(version = "V0.2", title = "The Giki! API"), produces = {
		"application/json" }, schemes = { SwaggerDefinition.Scheme.HTTP })
public class GikiAPIDocumentConfiguration {
}
