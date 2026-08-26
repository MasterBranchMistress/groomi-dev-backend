package org.groomi.groomidevbackend.auth.email_service.helpers

import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

 fun renderEmailTemplate(
    template: String,
    variables: Map<String, Any>,
    templateEngine: SpringTemplateEngine
): String {

    val context = Context()

    variables.forEach { (key, value) ->
        context.setVariable(key, value)
    }

    return templateEngine.process(template, context)
}