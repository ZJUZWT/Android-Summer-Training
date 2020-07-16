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

	@Query("UPDATE user SET login = 0 WHERE login = 1")
	void logout();

	@Insert
	long register(UserEntity userEntity);

	@Insert
	long addLike(LikeRelation likeRelation);

	@Query("DELETE FROM `like` WHERE user_id = :user_id AND videoURL = :videoURL ")
	void delLike(int user_id,String videoURL);

	@Insert
	long addFavor(FavorRelation favorRelation);

	@Query("DELETE FROM `favor` WHERE user_id = :user_id AND videoURL = :videoURL ")
	void delFavor(int user_id,String videoURL);

	@Query("SELECT * FROM `like` WHERE user_id = :user_id")
	List<LikeRelation> like(int user_id);

	@Query("SELECT * FROM `favor` WHERE user_id = :user_id")
	List<FavorRelation> favor(int user_id);

	@Query("SELECT * FROM `like` WHERE user_id = :user_id AND videoURL = :videoURL")
	List<LikeRelation> isLike(int user_id,String videoURL);

	@Query("SELECT * FROM `favor` WHERE user_id = :user_id AND videoURL = :videoURL")
	List<FavorRelation> isFavor(int user_id,String videoURL);

}
