/**
 * @Project :  스마트톨링정보시스템 구축
 * @Class : MailSvc.java
 * @Description : 
 *
 * @Author : LGCNS
 * @Since : 2017. 4. 20.
 *
 * @Copyright (c) 2018 EX All rights reserved.
 *-------------------------------------------------------------
 *              Modification Information
 *-------------------------------------------------------------
 * 날짜            수정자             변경사유 
 *-------------------------------------------------------------
 * 2018. 5. 25.        LGCNS             최초작성
 *-------------------------------------------------------------
 */

package batch.web.service.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import batch.web.base.BaseService;
import batch.web.exception.BizException;

@Service("mailSvi")
public class MailSvc extends BaseService implements MailSvi {

	@Resource(name = "mailSender")
	private JavaMailSender mailSender;

	@Override
	public Map<String, Object> sendMail(Map<String, Object> mail) throws Exception {

		MimeMessagePreparator preparator = getMessagePreparator(mail);
		try {
			mailSender.send(preparator);
		} catch (Exception ex) {
			LOGGER.error("Mail Send Error :: {}", ex.getMessage());
			throw new BizException("Mail Send Error");
		}
		return null;
	}

	private MimeMessagePreparator getMessagePreparator(final Map<String, Object> mail) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {

				InternetAddress[] fromAddrs = new InternetAddress[1];
				InternetAddress fromAddr = new InternetAddress(String.valueOf(mail.get("recipient")));
				fromAddrs[0] = fromAddr;
				mimeMessage.addFrom(fromAddrs);
				mimeMessage.setRecipient(Message.RecipientType.TO,
						new InternetAddress(String.valueOf(mail.get("recipient"))));
				mimeMessage.setText(String.valueOf(mail.get("message")));
				mimeMessage.setSubject(String.valueOf(mail.get("subject")));

				try {
					 MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
					 helper.setFrom(fromAddr);
					 helper.setTo(new InternetAddress(String.valueOf(mail.get("recipient"))));
					 helper.setSubject(String.valueOf(mail.get("subject")));
					 helper.setText(String.valueOf(mail.get("message")));
					 if (mail.get("fileNames") != null) {
						 @SuppressWarnings("unchecked")
						List<String> fileNames = (List<String>) mail.get("fileNames");
						 for (String fileName : fileNames) {
							 FileSystemResource file = new FileSystemResource(fileName);
					         helper.addAttachment(file.getFilename(), file);
						 }
					 }
				}
				catch(Exception ex) {
					LOGGER.error("Mail Send Error :: {}", ex.getMessage());
					throw new BizException(ex.getMessage());
				}

//				if (mail.get("fileName") != null) {
//					MimeBodyPart messageBodyPart = new MimeBodyPart();
//					Multipart multipart = new MimeMultipart();
//					messageBodyPart = new MimeBodyPart();
//					DataSource source = new FileDataSource(String.valueOf(mail.get("fileName")));
//					messageBodyPart.setDataHandler(new DataHandler(source));
//					String fileName = "";
//					
//					messageBodyPart.setFileName(fileName);
//			        
//					multipart.addBodyPart(messageBodyPart);
//			        mimeMessage.setContent(multipart);
//				}
			}
		};
		return preparator;
	}
}
