package com.zhoukaifan.simpleblog.vo;

public class DateType {
	private static final long serialVersionUID = -8317099881776204083L;
	private String date;
	private long blogCount;

	public DateType() {
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(long blogCount) {
		this.blogCount = blogCount;
	}

	public DateType(String date, long blogCount) {

		this.date = date;
		this.blogCount = blogCount;
	}

	public void addCount(){
		blogCount++;
	}
	public void subCount(){
		blogCount--;
	}
}
