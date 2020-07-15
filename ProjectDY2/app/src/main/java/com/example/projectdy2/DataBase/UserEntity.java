package com.example.projectdy2.DataBase;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "user"
//		foreignKeys = {
//		@ForeignKey(
//				entity = FavorRelation.class,
//				parentColumns = "user_id",
//				childColumns = "id",
//				onDelete = ForeignKey.CASCADE,
//				onUpdate = ForeignKey.CASCADE),
//		@ForeignKey(
//				entity = LikeRelation.class,
//				parentColumns = "user_id",
//				childColumns = "id",
//				onDelete = ForeignKey.CASCADE,
//				onUpdate = ForeignKey.CASCADE)}
				)
public class UserEntity {
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	private int id;

	@ColumnInfo(name = "username")
	private String username;

	@ColumnInfo(name = "password")
	private String password;

	@ColumnInfo(name = "login")
	private int login;

	public UserEntity(String username, String password, int login) {
		this.username = username;
		this.password = password;
		this.login = login;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getLogin() { return login; }
	public void setLogin(int login) { this.login = login; }
}

