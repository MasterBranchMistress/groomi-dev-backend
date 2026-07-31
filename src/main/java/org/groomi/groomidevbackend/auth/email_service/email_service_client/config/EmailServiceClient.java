package org.groomi.groomidevbackend.auth.email_service.email_service_client.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
public class EmailServiceClient {
    @Bean
    public SesV2Client sesV2Client(){
        return SesV2Client.builder().region(Region.US_EAST_2).build();
    }
}