package org.groomi.groomidevbackend.auth.email_service

import org.groomi.groomidevbackend.auth.email_service.email_service_client.config.EmailServiceClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sesv2.model.*

@Service
class EmailService private constructor(
    @Value($$"${test.email.sender}")
    var sender: String,
    private val sesClient: EmailServiceClient
) {
    fun sendEmail(
        recipient: String,
        subject: String,
        body: String
    ) {
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
                            .body(
                                Body.builder()
                                    .text(
                                        Content.builder()
                                            .data(body)
                                            .build()
                                    )
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()
        sesClient.sesV2Client().sendEmail(request)
    }
}