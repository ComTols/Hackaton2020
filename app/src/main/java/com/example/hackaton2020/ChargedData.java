package com.example.hackaton2020;

import java.util.ArrayList;

public class ChargedData {

	public static class User {
		public boolean messageLetter;
		public boolean messageMail;

		//Eigener Account
		public String forename;
		public String name;
		public String street;
		public String postcode;
		public String city;
		public String mail;

		public int id;

		public User(boolean messageLetter, boolean messageMail, String forename, String name, String street, String postcode, String city, String mail, int id) {
			this.messageLetter = messageLetter;
			this.messageMail = messageMail;
			this.forename = forename;
			this.name = name;
			this.street = street;
			this.postcode = postcode;
			this.city = city;
			this.mail = mail;
			this.id = id;
		}

		public User() {}

		public boolean hasID(int id) {
			return id == this.id;
		}
	}
	public static class Events {
		public String type;
		public String id;
		public String name;
		public String street;
		public String plz;
		public String city;
		public String details;
		public Long startTime;
		public ArrayList<User> invitetUsers;

		public Events(String type, String id, String name, String street, String plz, String city, String details, Long startTime, ArrayList<User> invitetUsers) {
			this.type = type;
			this.name = name;
			this.street = street;
			this.plz = plz;
			this.city = city;
			this.details = details;
			this.startTime = startTime;
			this.invitetUsers = invitetUsers;;

		}

		public Events() {
			this.startTime = System.currentTimeMillis();
		}

		public void qrToEvent(String[] qrResult) {
			type = qrResult[0];
			id = qrResult[1];
			name = qrResult[2];
			street = qrResult[3];
			plz = qrResult[4];
			city = qrResult[5];
			details = qrResult[6];
		}
	}

	//Eigener Account
	private User self;
	private boolean messagePush;
	private boolean messageInApp;

	//Andere Nutzer
	private ArrayList<User> otherUsers = new ArrayList<>();
	private int nextUserId;

	//Events
	private ArrayList<Events> events = new ArrayList<>();

	//------------------ Getter und Setter ------------------

	public boolean isMessagePush() {
		return messagePush;
	}

	public void setMessagePush(boolean messagePush) {
		this.messagePush = messagePush;
	}

	public boolean isMessageInApp() {
		return messageInApp;
	}

	public void setMessageInApp(boolean messageInApp) {
		this.messageInApp = messageInApp;
	}

	public ArrayList<User> getOtherUsers() {
		return this.otherUsers;
	}

	public void setOtherUsers(ArrayList<User> otherUsers) {
		this.otherUsers = otherUsers;
	}

	public ArrayList<Events> getEvents() {
		return this.events;
	}

	public void setEvents(ArrayList<Events> events) {
		this.events = events;
	}

	public User getSelf() {
		return self;
	}

	public void setSelf(User self) {
		this.self = self;
	}

	public void addEvent(String type, String id, String name, String street, String plz, String city, String details, int[] otherUserIDs) {
		Events newEvent = new Events(type,id, name,street,plz,city,details,System.currentTimeMillis(),null);
		for(int i = 0; i < otherUsers.size(); i++) {
			for(int j = 0; j < otherUserIDs.length; j++) {
				if(otherUsers.get(i).hasID(otherUserIDs[j])) {
					newEvent.invitetUsers.add(otherUsers.get(i));
				}
			}
		}
		events.add(newEvent);
	}

	public int getOtherUserIdByFullName(String fullName) {
		for(int i = 0; i < otherUsers.size(); i++) {
			User user = otherUsers.get(i);
			if((user.forename + " " + user.name).equals(fullName)) {
				return user.id;
			}
		}
		return -1;
	}

	public void addOtherUser(User user) {
		this.otherUsers.add(user);
	}
}
