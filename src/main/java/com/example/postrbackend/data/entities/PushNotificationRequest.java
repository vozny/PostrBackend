package com.example.postrbackend.data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationRequest {
	private String title;
	private String message;
	private String topic;
	private String token;

}
