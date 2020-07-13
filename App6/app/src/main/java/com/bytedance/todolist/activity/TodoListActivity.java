package com.bytedance.todolist.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.bytedance.todolist.database.DateConverter;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.View;

import com.bytedance.todolist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity implements ToFragmentListener{

    private TodoListAdapter mAdapter;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TodoListAdapter();
        recyclerView.setAdapter(mAdapter);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                Intent intent = new Intent(view.getContext(),TakeANote.class);
//                Bundle bundle = new Bundle();
//                bundle.put
//                startActivity(intent);
                startActivityForResult(intent,1);
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                        dao.deleteAll();
                        for (int i = 0; i < 20; ++i) {
                            dao.addTodo(new TodoListEntity("This is " + i + " item", new Date(System.currentTimeMillis())));
                        }
                        final List<TodoListEntity> entityList = dao.loadAll();
                        entityList.sort(new Comparator<TodoListEntity>() {
                            @Override
                            public int compare(TodoListEntity o1, TodoListEntity o2) {
                                return DateConverter.toTimeStamp(o1.getTime()) > DateConverter.toTimeStamp(o2.getTime()) ? 1 : 0  ;
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setData(entityList);
                                mAdapter.notifyItemChanged(0);
                            }
                        });
                        Snackbar.make(mFab, R.string.hint_insert_complete, Snackbar.LENGTH_SHORT).show();
                    }
                }.start();
                return true;
            }
        });
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                final List<TodoListEntity> entityList = dao.loadAll();
                entityList.sort(new Comparator<TodoListEntity>() {
                    @Override
                    public int compare(TodoListEntity o1, TodoListEntity o2) {
                        return DateConverter.toTimeStamp(o1.getTime()) > DateConverter.toTimeStamp(o2.getTime()) ? 1 : 0  ;
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(entityList);
//                        mAdapter.notifyItemChanged(0,entityList.size());
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;
            final TodoListEntity entity = new TodoListEntity(data.getStringExtra("content"), DateConverter.fromTimeStamp(System.currentTimeMillis()));

            new Thread() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                    dao.addTodo(entity);
                    final List<TodoListEntity> entityList = dao.loadAll();
                    entityList.sort(new Comparator<TodoListEntity>() {
                        @Override
                        public int compare(TodoListEntity o1, TodoListEntity o2) {
                            return DateConverter.toTimeStamp(o1.getTime()) > DateConverter.toTimeStamp(o2.getTime()) ? 1 : 0  ;
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            mAdapter.notifyDataSetChanged();
                            mAdapter.setData(entityList);
                            mAdapter.notifyItemChanged(0,entityList.size());
                        }
                    });
                }
            }.start();
        }
    }

    @Override
    public void onTypeClick(final Long id) {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                dao.delete(id);
                final List<TodoListEntity> entityList = dao.loadAll();
                entityList.sort(new Comparator<TodoListEntity>() {
                    @Override
                    public int compare(TodoListEntity o1, TodoListEntity o2) {
                        return DateConverter.toTimeStamp(o1.getTime()) > DateConverter.toTimeStamp(o2.getTime()) ? 1 : 0  ;
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                            mAdapter.notifyDataSetChanged();
                        mAdapter.setData(entityList);
                        mAdapter.notifyItemChanged(0,entityList.size());
                    }
                });
            }
        }.start();
    }

    @Override
    public void onChangeDone(final Long id) {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                dao.changeDone(id);
                final List<TodoListEntity> entityList = dao.loadAll();
                entityList.sort(new Comparator<TodoListEntity>() {
                    @Override
                    public int compare(TodoListEntity o1, TodoListEntity o2) {
                        return DateConverter.toTimeStamp(o1.getTime()) > DateConverter.toTimeStamp(o2.getTime()) ? 1 : 0  ;
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(entityList);
                        mAdapter.notifyItemChanged(0,entityList.size());
                    }
                });
            }
        }.start();
    }
}
