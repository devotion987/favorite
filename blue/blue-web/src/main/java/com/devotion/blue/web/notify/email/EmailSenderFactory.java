package com.devotion.blue.web.notify.email;

public class EmailSenderFactory {

	static IEmailSender sender;

	public static IEmailSender createSender() {

		if (sender == null) {
			sender = new SimplerEmailSender();
		}
		return sender;
	}

}
