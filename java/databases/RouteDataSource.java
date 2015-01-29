package databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabo on 1/26/15.
 */
public class RouteDataSource {
    public static final String TABLE_ROUTES = "routes";
    public static final String ROUTES_ID = "_id";
    public static final String ROUTES_AVAILABILITY = "availability";
    public static final String ROUTES_DATE = "date";
    public static final String ROUTES_COST = "cost";
    public static final String ROUTES_TYPE = "type";
    public static final String ROUTES_CAR_ID = "route_id";
    public static final String ROUTES_USER_ID = "user_id";

    private String[] allColumns = {
            ROUTES_ID, ROUTES_AVAILABILITY, ROUTES_DATE, ROUTES_COST, ROUTES_TYPE, ROUTES_CAR_ID, ROUTES_USER_ID
    };
    private SQLiteDatabase database;

    public static String createTableQuery(){
        return "CREATE TABLE " + TABLE_ROUTES + " ("
                +ROUTES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ROUTES_AVAILABILITY + " INTEGER, "
                + ROUTES_DATE + " TEXT, "
                + ROUTES_COST + " REAL, "
                + ROUTES_TYPE + " TEXT, "
                + ROUTES_CAR_ID + " INTEGER NOT NULL,"
                + ROUTES_USER_ID + " INTEGER NOT NULL, "
                + "FOREIGN KEY ("+ROUTES_CAR_ID+") REFERENCES "
                + CarDataSource.TABLE_CARS + " ("+CarDataSource.CARS_ID+"),"
                + "FOREIGN KEY ("+ROUTES_USER_ID+") REFERENCES "
                + UserDataSource.TABLE_USERS + " ("+UserDataSource.USER_ID+"));";


    }

    public RouteDataSource(SQLiteDatabase database){
        this.database = database;
    }

    public Route createRoute(int availability, String date, Double cost, int type, long car_id, long user_id){
        ContentValues values = new ContentValues();
        values.put(ROUTES_AVAILABILITY, availability);
        values.put(ROUTES_DATE, date);
        values.put(ROUTES_COST, cost);
        values.put(ROUTES_TYPE, type);
        values.put(ROUTES_CAR_ID, car_id);
        values.put(ROUTES_USER_ID, user_id);
        long insertID = database.insert(TABLE_ROUTES,null,values);
        Cursor cursor = database.query(TABLE_ROUTES,allColumns,ROUTES_ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Route newRoute = cursorToRoute(cursor);
        cursor.close();
        return newRoute;
    }

    public void deleteRoute(Route route) {
        long id = route.getId();
        System.out.println("Route deleted with id: " + id);
        database.delete(TABLE_ROUTES, ROUTES_ID + " = " + id, null);
    }

    public Route getRouteById(long id){
        Cursor cursor = database.query(TABLE_ROUTES,allColumns,ROUTES_ID + " = " + id, null,null,null,null);
        cursor.moveToFirst();
        Route newRoute = cursorToRoute(cursor);
        cursor.close();
        return newRoute;
    }

    public List<Route> getAllRoutesByUser(long id){
        List<Route> routes = new ArrayList<>();
        Cursor cursor = database.query(TABLE_ROUTES, allColumns, ROUTES_USER_ID + " = " + id, null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Route route = cursorToRoute(cursor);
            routes.add(route);
            cursor.moveToNext();
        }
        cursor.close();
        return routes;
    }

    private Route cursorToRoute(Cursor cursor) {
        Route route = new Route();
        route.setId(cursor.getLong(0));
        route.setAvailability(cursor.getInt(1));
        route.setDate(cursor.getString(2));
        route.setCost(cursor.getDouble(3));
        route.setType(cursor.getInt(4));
        route.setCar_id(cursor.getLong(5));
        route.setUser_id(cursor.getLong(6));
        return route;
    }
}
