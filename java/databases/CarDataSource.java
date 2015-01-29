package databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabo on 1/26/15.
 */
public class CarDataSource {
    public static final String TABLE_CARS = "cars";
    public static final String CARS_ID = "_id";
    public static final String CARS_BRAND = "brand";
    public static final String CARS_MODEL = "model";
    public static final String CARS_PLATE = "plate";
    public static final String CARS_COLOR = "color";
    public static final String CARS_USER_ID = "user_id";
    private final String[] allColumns = {
            CARS_ID, CARS_BRAND, CARS_MODEL, CARS_PLATE, CARS_COLOR, CARS_USER_ID
    };
    private SQLiteDatabase database;

    public static String createTableQuery(){
        return "CREATE TABLE " + TABLE_CARS + " ("
                +CARS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CARS_BRAND + " TEXT, "
                + CARS_MODEL + " TEXT, "
                + CARS_PLATE + " TEXT, "
                + CARS_COLOR + " TEXT, "
                + CARS_USER_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY ("+CARS_USER_ID+") REFERENCES "
                + UserDataSource.TABLE_USERS + " ("+UserDataSource.USER_ID+"));";


    }

    public CarDataSource(SQLiteDatabase database){
        this.database = database;
    }

    public Car createCar(String brand, String model, String plate, String color, long user_id){
        ContentValues values = new ContentValues();
        values.put(CARS_BRAND, brand);
        values.put(CARS_MODEL, model);
        values.put(CARS_PLATE, plate);
        values.put(CARS_COLOR, color);
        values.put(CARS_USER_ID, user_id);
        long insertID = database.insert(TABLE_CARS,null,values);
        Cursor cursor = database.query(TABLE_CARS,allColumns,CARS_ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Car newCar = cursorToCar(cursor);
        cursor.close();
        return newCar;
    }

    public void deleteCar(Car car) {
        long id = car.getId();
        System.out.println("Car deleted with id: " + id);
        database.delete(TABLE_CARS, CARS_ID + " = " + id, null);
    }

    public Car getCarById(long id){
        Cursor cursor = database.query(TABLE_CARS,allColumns,CARS_ID + " = " + id, null,null,null,null);
        cursor.moveToFirst();
        Car newCar = cursorToCar(cursor);
        cursor.close();
        return newCar;
    }

    public List<Car> getAllCarsByUser(long id){
        List<Car> cars = new ArrayList<>();
        Cursor cursor = database.query(TABLE_CARS, allColumns, CARS_USER_ID + " = " + id, null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Car car = cursorToCar(cursor);
            cars.add(car);
            cursor.moveToNext();
        }
        cursor.close();
        return cars;
    }

    private Car cursorToCar(Cursor cursor) {
        Car car = new Car();
        car.setId(cursor.getLong(0));
        car.setBrand(cursor.getString(1));
        car.setModel(cursor.getString(2));
        car.setPlate(cursor.getString(3));
        car.setColor(cursor.getString(4));
        car.setUser_id(cursor.getLong(5));
        return car;
    }

}
