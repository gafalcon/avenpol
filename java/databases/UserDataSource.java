package databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabo on 1/26/15.
 */
public class UserDataSource {
    public static final String TABLE_USERS = "users";
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    private String[] allColumns = {USER_ID,
                                   USER_NAME,
                                   USER_EMAIL,
                                   USER_PASSWORD};
    private SQLiteDatabase database;

    public static String createTableQuery(){
        return "create table "
                + TABLE_USERS + "("
                + USER_ID + " integer primary key autoincrement, "
                + USER_NAME + " text not null, "
                + USER_EMAIL + " text not null, "
                + USER_PASSWORD + " text not null);";

    }

    public UserDataSource(SQLiteDatabase database){
        this.database = database;
    }

    public User createUser(String name, String email, String password){
        ContentValues values = new ContentValues();
        values.put(USER_NAME, name);
        values.put(USER_EMAIL,email);
        values.put(USER_PASSWORD, password);
        long insertID = database.insert(TABLE_USERS,null,values);
        Cursor cursor = database.query(TABLE_USERS,allColumns,USER_ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public void deleteUser(User user) {
        long id = user.getId();
        System.out.println("User deleted with id: " + id);
        database.delete(TABLE_USERS, USER_ID + " = " + id, null);
    }

    public User getUserById(long id){
        Cursor cursor = database.query(TABLE_USERS,allColumns,USER_ID + " = " + id, null,null,null,null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        Cursor cursor = database.query(TABLE_USERS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();
        return users;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));
        user.setName(cursor.getString(1));
        user.setEmail(cursor.getString(2));
        user.setPassword(cursor.getString(3));
        return user;
    }


}
