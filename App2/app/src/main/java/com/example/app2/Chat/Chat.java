package com.example.app2.Chat;

public class Chat {

	private int type ; // bot 1 human -1
	private int iconId ;
	private String sentence ;
	public int BOT = 1 ;
	public int HUMAN = -1 ;

	public Chat(String sentence,int iconId,int type) {
		this.iconId = iconId ;
		this.sentence = sentence ;
		this.type = type ;
	}

	public int getType() { return type ; }
	public int getIconId(){
		return iconId ;
	}
	public String getSentence(){
		return sentence ;
	}

}
