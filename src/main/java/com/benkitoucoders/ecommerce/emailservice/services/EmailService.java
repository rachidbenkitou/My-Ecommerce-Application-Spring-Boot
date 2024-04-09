package com.benkitoucoders.ecommerce.emailservice.services;

import com.benkitoucoders.ecommerce.emailservice.dtos.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
