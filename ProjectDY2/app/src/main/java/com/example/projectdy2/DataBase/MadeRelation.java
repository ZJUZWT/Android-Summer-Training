package com.example.projectdy2.DataBase;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "made",
		primaryKeys = {"user_id","videoURL"})
public class MadeRelation {
	@NonNull
	@ColumnInfo(name = "user_id")
	private int id;

	@NonNull
	@ColumnInfo(name = "videoURL")
	private String videoLink;

	public MadeRelation(int id, String videoLink) {
		this.id = id;
		this.videoLink = videoLink;
	}

	public String getVideoLink() {
		return videoLink;
	}
	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
