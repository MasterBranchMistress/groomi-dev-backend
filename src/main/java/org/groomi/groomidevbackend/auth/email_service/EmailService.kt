package org.groomi.groomidevbackend.auth.email_service

import org.groomi.groomidevbackend.auth.email_service.email_service_client.config.EmailServiceClient
import org.groomi.groomidevbackend.auth.email_service.helpers.renderEmailTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.thymeleaf.spring6.SpringTemplateEngine
import software.amazon.awssdk.services.sesv2.model.*

@Service
class EmailService(
    @Value("\${test.email.sender}")
    var sender: String,
    private val sesClient: EmailServiceClient,
    private val templateEngine: SpringTemplateEngine

) {
    fun sendEmail(
        recipient: String,
        subject: String,
        body: String,
        isHtml: Boolean = false
    ) {
        val bodyBuilder = Body.builder()

        if (isHtml) {
            bodyBuilder.html(
                Content.builder()
                    .data(body)
                    .build()
            )
        } else {
            bodyBuilder.text(
                Content.builder()
                    .data(body)
                    .build()
            )
        }

        val request = SendEmailRequest.builder()
            .fromEmailAddress(sender)
            .destination(
                Destination.builder()
                    .toAddresses(recipient)
                    .build()
            )
            .content(
                EmailContent.builder()
                    .simple(
                        Message.builder()
                            .subject(
                                Content.builder()
                                    .data(subject)
                                    .build()
                            )
                            .body(bodyBuilder.build())
                            .build()
                    )
                    .build()
            )
            .build()

        sesClient.sesV2Client().sendEmail(request)
    }
    fun sendPasswordResetEmail(
        recipient: String,
        firstName: String,
        resetLink: String
    ) {
        val htmlBody = renderEmailTemplate(
            "email_templates/reset-password",
            mapOf(
                "firstName" to firstName,
                "resetLink" to resetLink
            ),
            templateEngine
        )

        sendEmail(
            recipient,
            "Groomr - Reset Your Password",
            htmlBody,
            true
        )
    }
}