import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.Executors;


@Service
public class EmailUtils {
    private Logger logger = LoggerFactory.getLogger(EmailUtils.class);


    @Value("${email.server.host}")
    private String emailServerHost;

    @Value("${email.server.port}")
    private int emailServerPort;

    @Value("${email.server.username}")
    private String emailServerUsername;

    @Value("${email.server.password}")
    private String emailServerPassword;

    @Value("${email.server.from}")
    private String emailServerFrom;

    @Value("${email.server.admin}")
    private java.util.List<String> emailServerAdmin;

    @Value("${email.notice.enable}")
    private Boolean emailCallCountNoticeEnable;

    private static java.util.concurrent.ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void send(String title, String content) {
        if (emailCallCountNoticeEnable) {
            if (Objects.nonNull(emailServerAdmin) && !emailServerAdmin.isEmpty()) {
                emailServerAdmin.forEach(admin -> {
                    EmailSender sender = new EmailSender(title, content, admin);
                    executorService.execute(sender);
                });
            }
        } else {
            logger.warn("Mail notification switch is not on");
        }
    }

    class EmailSender extends Thread {
        private String toEmail;
        private String title;
        private String content;

        public EmailSender(String title, String content, String toEmail) {
            this.title = title;
            this.content = content;
            this.toEmail = toEmail;
        }

        @Override
        public void run() {
            Email email = new SimpleEmail();
            try {
                if (StringUtils.hasText(this.toEmail)) {
                    email.setHostName(emailServerHost);
                    email.setSmtpPort(emailServerPort);
                    email.setAuthenticator(new DefaultAuthenticator(emailServerUsername, emailServerPassword));
                    email.setFrom(emailServerFrom);
                    String[] tos = this.toEmail.split(",");
                    for (String to : tos) {
                        email.addTo(to);
                    }

                    email.setCharset("UTF-8");
                    email.setSubject(this.title);
                    email.buildMimeMessage();
                    email.getMimeMessage().setText(content, "UTF-8");
                    email.sendMimeMessage();
                    logger.info("Send email successfully");
                }
            } catch (Exception e) {
                logger.error("Send email error", e);
            }
        }
    }
}
