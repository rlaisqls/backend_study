package com.practice.shoppingmall.domain.user.service.mail;

import com.practice.shoppingmall.domain.user.presentation.dto.request.SendMailRequest;


public interface MailService {
    String sendEmail(SendMailRequest request);
}