package databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabo on 1/26/15.
 */
public class RequestDataSource {
    public static final String TABLE_REQUESTS = "requests";
    public static final String REQUESTS_ID = "_id";
    public static final String REQUESTS_STATE = "state";
    public static final String REQUESTS_USER_ID = "user_id";
    public static final String REQUESTS_ROUTE_ID = "route_id";
    public String[] allColumns = {
            REQUESTS_ID, REQUESTS_STATE, REQUESTS_USER_ID, REQUESTS_ROUTE_ID
    };
    public SQLiteDatabase database;

    public static String createTableQuery(){
        return "CREATE TABLE " + TABLE_REQUESTS + " ("
                + REQUESTS_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + REQUESTS_STATE + " INTEGERS, "
                + REQUESTS_USER_ID + " INTEGER NOT NULL, "
                + REQUESTS_ROUTE_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY ("+ REQUESTS_USER_ID + ") REFERENCES "
                + UserDataSource.TABLE_USERS + " ("+ UserDataSource.USER_ID+"), "
                + "FOREIGN KEY (" + REQUESTS_ROUTE_ID + ") REFERENCES "
                + RouteDataSource.TABLE_ROUTES + " ("+ RouteDataSource.ROUTES_ID+ "));";


    }

    public RequestDataSource(SQLiteDatabase database){
        this.database = database;
    }

    public Request createRequest(int state, long route_id, long user_id){
        ContentValues values = new ContentValues();
        values.put(REQUESTS_STATE, state);
        values.put(REQUESTS_USER_ID, user_id);
        values.put(REQUESTS_ROUTE_ID, route_id);
        long insertID = database.insert(TABLE_REQUESTS,null,values);
        Cursor cursor = database.query(TABLE_REQUESTS,allColumns,REQUESTS_ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Request newRequest = cursorToRequest(cursor);
        cursor.close();
        return newRequest;
    }

    public void deleteRequest(Request request) {
        long id = request.getId();
        System.out.println("Request deleted with id: " + id);
        database.delete(TABLE_REQUESTS, REQUESTS_ID + " = " + id, null);
    }

    public Request getRequestById(long id){
        Cursor cursor = database.query(TABLE_REQUESTS,allColumns,REQUESTS_ID + " = " + id, null,null,null,null);
        cursor.moveToFirst();
        Request newRequest = cursorToRequest(cursor);
        cursor.close();
        return newRequest;
    }

    public List<Request> getAllRequestsByUser(long id){
        List<Request> requests = new ArrayList<>();
        Cursor cursor = database.query(TABLE_REQUESTS, allColumns, REQUESTS_USER_ID + " = " + id, null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Request request = cursorToRequest(cursor);
            requests.add(request);
            cursor.moveToNext();
        }
        cursor.close();
        return requests;
    }

    public List<Request> getAllRequestsByRoute(long id){
        List<Request> requests = new ArrayList<>();
        Cursor cursor = database.query(TABLE_REQUESTS, allColumns, REQUESTS_ROUTE_ID + " = " + id, null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Request request = cursorToRequest(cursor);
            requests.add(request);
            cursor.moveToNext();
        }
        cursor.close();
        return requests;
    }

    private Request cursorToRequest(Cursor cursor) {
        Request request = new Request();
        request.setId(cursor.getLong(0));
        request.setState(cursor.getInt(1));
        request.setUser_id(cursor.getLong(2));
        request.setRoute_id(cursor.getLong(3));
        return request;
    }

}
