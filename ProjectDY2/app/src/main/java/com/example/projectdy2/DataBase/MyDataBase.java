package com.example.projectdy2.DataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {LikeRelation.class,UserEntity.class,FavorRelation.class,MadeRelation.class}, version = 2, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {
	private static volatile MyDataBase INSTANCE;
	public abstract QueryDao queryDao();

	public MyDataBase() {
	}

	public static MyDataBase inst(Context context) {
		if (INSTANCE == null) {
			synchronized (MyDataBase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDataBase.class, "dy.db").addMigrations(migration).build();
				}
			}
		}
		return INSTANCE;
	}

	static Migration migration = new Migration(1,2) {
		@Override
		public void migrate(@NonNull SupportSQLiteDatabase database) {
			database.execSQL("CREATE TABLE made (user_id INTEGER NOT NULL , videoURL TEXT NOT NULL , PRIMARY KEY(user_id,videoURL))");
		}
	};

}
