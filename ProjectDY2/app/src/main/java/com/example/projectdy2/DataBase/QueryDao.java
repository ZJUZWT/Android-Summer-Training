package com.example.projectdy2.DataBase;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface QueryDao {
	//TODO：
	// 查询登录 hasLogin()
	// 查询用户 userInfo()
	@Query("SELECT * FROM user WHERE login = 1")
	List<UserEntity> hasLogin() ;

	@Query("SELECT * FROM user WHERE username = :username")
	List<UserEntity> userInfo(String username) ;

	@Query("UPDATE user SET login = 1 WHERE username = :username")
	void login(String username);

	@Insert
	long register(UserEntity userEntity);
}
