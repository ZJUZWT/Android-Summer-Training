package com.example.projectdy2.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {LikeRelation.class,UserEntity.class,FavorRelation.class}, version = 1, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
	private static volatile MyDataBase INSTANCE;
	public abstract QueryDao queryDao();

	public MyDataBase() {
	}

	public static MyDataBase inst(Context context) {
		if (INSTANCE == null) {
			synchronized (MyDataBase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDataBase.class, "dy.db").build();

				}
			}
		}
		return INSTANCE;
	}
}
