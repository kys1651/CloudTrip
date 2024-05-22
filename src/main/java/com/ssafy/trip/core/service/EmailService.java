package com.ssafy.trip.core.service;

import com.ssafy.trip.core.exception.CustomException;
import com.ssafy.trip.core.exception.ErrorCode;
import com.ssafy.trip.domain.auth.repository.AuthCacheRepository;
import com.ssafy.trip.domain.schedule.dto.ScheduleData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final AuthCacheRepository authCacheRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    public void sendScheduleInviteEmail(String code, ScheduleData.Invite invite, String scheduleId) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("CLOUD TRIP 일정 초대 메일입니다.");
            helper.setTo(invite.getUsername());

            String confirmURI = createURI("schedule-invite", code, invite.getUsername(), scheduleId);

            Context context = new Context();
            context.setVariable("confirmURI", confirmURI);
            context.setVariable("owner", invite.getOwner());
            context.setVariable("name", invite.getName());

            String html = templateEngine.process("mail/confirm_schedule_invite_mail", context);
            helper.setText(html, true);

            sendEmail(message, invite.getUsername());
        } catch (MessagingException e) {
            log.error("이메일 전송에 실패했습니다. 다시 시도해주세요.", e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public void sendSignUpEmail(String to, String nickname) {
        String code = generateCode();
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("CLOUD TRIP 회원가입 인증 메일입니다.");
            helper.setTo(to);


            String confirmURI = createURI("sign-up", code, to, "");

            Context context = new Context();
            context.setVariable("confirmURI", confirmURI);
            context.setVariable("nickname", nickname);

            String html = templateEngine.process("mail/confirm_member_account_mail", context);
            helper.setText(html, true);

            sendEmail(message, to);
            authCacheRepository.saveConfirm(to, code);
        } catch (MessagingException e) {
            log.error("이메일 전송에 실패했습니다. 다시 시도해주세요.", e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    public void sendFindPasswordEmail(String to) {
        String code = generateCode();
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setSubject("CLOUD TRIP 비밀번호 찾기 메일입니다.");
            helper.setTo(to);

            Context context = new Context();
            context.setVariable("code", code);

            String html = templateEngine.process("mail/find_member_account_mail", context);
            helper.setText(html, true);

            sendEmail(message, to);
            authCacheRepository.saveFindPassword(to, code);
        } catch (MessagingException e) {
            log.error("이메일 전송에 실패했습니다. 다시 시도해주세요.", e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    private void sendEmail(MimeMessage message, String to) {
        new Thread(() -> {
            try {
                javaMailSender.send(message);
                log.info("{} 으로 이메일 전송에 성공했습니다.", to);
            } catch (Exception e) {
                log.error("이메일 전송에 실패했습니다. 다시 시도해주세요.", e);
            }
        }).start();
    }

    private String createURI(String type, String code, String email, String id) {
        return UriComponentsBuilder.fromUriString(frontendUrl + "/confirm/email")
                .queryParam("type", type)
                .queryParam("code", code)
                .queryParam("email", email)
                .queryParam("id", id)
                .toUriString();
    }

    private String generateCode() {
        return String.valueOf((int) (Math.random() * 899999) + 100000);
    }
}
