package com.example.postrbackend.services.implementations;

import com.example.postrbackend.data.NotificationParameter;
import com.example.postrbackend.data.entities.PushNotificationRequest;
import com.example.postrbackend.services.FCMService;
import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@Service
public class FCMServiceImpl implements FCMService {

	private Logger logger = LoggerFactory.getLogger(com.example.postrbackend.services.implementations.FCMServiceImpl.class);

	@Override
	public void sendMessageToToken(PushNotificationRequest request)
			throws InterruptedException, ExecutionException {
		Message message = getPreconfiguredMessageToToken(request);
		String response = sendAndGetResponse(message);
		logger.info("Sent message to token. Device token: " + request.getToken() + ", " + response);
	}
	@Override
	public String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
		return FirebaseMessaging.getInstance().sendAsync(message).get();
	}
	@Override
	public AndroidConfig getAndroidConfig(String topic) {
		return AndroidConfig.builder()
				.setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
				.setPriority(AndroidConfig.Priority.HIGH)
				.setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
						.setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
	}
	@Override
	public ApnsConfig getApnsConfig(String topic) {
		return ApnsConfig.builder()
				.setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
	}
	@Override
	public Message getPreconfiguredMessageToToken(PushNotificationRequest request) {
		return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
				.build();
	}
	@Override
	public Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
		AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
		ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
		return Message.builder()
				.setApnsConfig(apnsConfig).setAndroidConfig(androidConfig).setNotification(
						new Notification(request.getTitle(), request.getMessage()));
	}


}