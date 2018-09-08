package com.zhoukaifan.simpleblog.domain;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ContactMessage{
	@NotNull(message = "ContactMessage.name.null")
	@Size(min = 1,max = 256,message = "ContactMessage.name.length")
	private String name;
	@NotNull(message = "ContactMessage.name.null")
	@Size(min = 1,max = 256,message = "ContactMessage.name.length")
	private String email;
	@NotNull(message = "ContactMessage.name.null")
	@Size(min = 1,max = 256,message = "ContactMessage.name.length")
	private String title;
	@NotNull(message = "ContactMessage.name.null")
	@Size(min = 1,max = 20000,message = "ContactMessage.name.length")
	private String content;
	private Date date = new Date();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
