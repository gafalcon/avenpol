package databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabo on 1/26/15.
 */
public class PointDataSource {
    public final static String TABLE_POINTS = "points";
    public final static String POINTS_ID = "_id";
    public final static String POINTS_LATITUDE = "latitude";
    public final static String POINTS_LONGITUDE = "longitude";
    public final static String POINTS_ORDER = "point_order";
    public final static String POINTS_ROUTE_ID = "route_id";
    private String[] allColumns = {
            POINTS_ID,POINTS_LATITUDE,POINTS_LONGITUDE, POINTS_ORDER, POINTS_ROUTE_ID
    };
    private SQLiteDatabase database;

    public static String createTableQuery(){
        return "CREATE TABLE " + TABLE_POINTS + " ("
                + POINTS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + POINTS_LATITUDE + " REAL, "
                + POINTS_LONGITUDE + " REAL, "
                + POINTS_ORDER + " INTEGER, "
                + POINTS_ROUTE_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY ("+POINTS_ROUTE_ID+") REFERENCES "
                + RouteDataSource.TABLE_ROUTES + " ("+RouteDataSource.ROUTES_ID+"));";
    }

    public PointDataSource(SQLiteDatabase database){
        this.database = database;
    }

    public Point createPoint(Double latitude, Double longitude, int order, long route_id ){
        ContentValues values = new ContentValues();
        values.put(POINTS_LATITUDE, latitude);
        values.put(POINTS_LONGITUDE, longitude);
        values.put(POINTS_ORDER, order);
        values.put(POINTS_ROUTE_ID, route_id);
        long insertID = database.insert(TABLE_POINTS,null,values);
        Cursor cursor = database.query(TABLE_POINTS,allColumns,POINTS_ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Point newPoint = cursorToPoint(cursor);
        cursor.close();
        return newPoint;
    }

    public void deletePoint(Point point) {
        long id = point.getId();
        System.out.println("Point deleted with id: " + id);
        database.delete(TABLE_POINTS, POINTS_ID + " = " + id, null);
    }

    public Point getPointById(long id){
        Cursor cursor = database.query(TABLE_POINTS,allColumns,POINTS_ID + " = " + id, null,null,null,null);
        cursor.moveToFirst();
        Point newPoint = cursorToPoint(cursor);
        cursor.close();
        return newPoint;
    }

    public List<LatLng> getAllPointsByRoute(long id){
        List<LatLng> points = new ArrayList<>();
        Cursor cursor = database.query(TABLE_POINTS, allColumns, POINTS_ROUTE_ID + " = " + id,null,null,null,POINTS_ORDER);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            LatLng point = cursorToLatLng(cursor);
            points.add(point);
            cursor.moveToNext();
        }
        cursor.close();
        return points;
    }

    private Point cursorToPoint(Cursor cursor) {
        Point point = new Point();
        point.setId(cursor.getLong(0));
        point.setLatitude(cursor.getDouble(1));
        point.setLongitude(cursor.getDouble(2));
        point.setOrder(cursor.getInt(3));
        point.setRoute_id(cursor.getLong(4));
        return point;
    }

    private LatLng cursorToLatLng(Cursor cursor) {
        LatLng latLng = new LatLng(cursor.getDouble(1),cursor.getDouble(2));
        return  latLng;
    }
}
