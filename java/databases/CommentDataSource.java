package databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabo on 1/26/15.
 */
public class CommentDataSource {
    public static final String TABLE_COMMENTS = "comments";
    public static final String COMMENTS_ID = "_id";
    public static final String COMMENTS_SUBJECT = "subject";
    public static final String COMMENTS_CONTENT = "content";
    public static final String COMMENTS_ROUTE_ID = "route_id";
    private final String[] allColumns = {
            COMMENTS_ID, COMMENTS_SUBJECT, COMMENTS_CONTENT, COMMENTS_ROUTE_ID
    };
    private SQLiteDatabase database;

    public static String createTableQuery(){
        return "CREATE TABLE " + TABLE_COMMENTS + " ("
                + COMMENTS_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COMMENTS_CONTENT + " TEXT, "
                + COMMENTS_SUBJECT + " TEXT, "
                + COMMENTS_ROUTE_ID + " INTEGER NOT NULL,"
                + "FOREIGN KEY ("+COMMENTS_ROUTE_ID+") REFERENCES "
                + RouteDataSource.TABLE_ROUTES + " ("+RouteDataSource.ROUTES_ID+"));";


    }

    public CommentDataSource(SQLiteDatabase database){
        this.database = database;
    }

    public Comment createComment(String subject, String content, long route_id){
        ContentValues values = new ContentValues();
        values.put(COMMENTS_SUBJECT, subject);
        values.put(COMMENTS_CONTENT, content);
        values.put(COMMENTS_ROUTE_ID, route_id);
        long insertID = database.insert(TABLE_COMMENTS,null,values);
        Cursor cursor = database.query(TABLE_COMMENTS,allColumns,COMMENTS_ID + " = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(TABLE_COMMENTS, COMMENTS_ID + " = " + id, null);
    }

    public Comment getCommentById(long id){
        Cursor cursor = database.query(TABLE_COMMENTS,allColumns,COMMENTS_ID + " = " + id, null,null,null,null);
        cursor.moveToFirst();
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    public List<Comment> getAllCommentsByRoute(long id){
        List<Comment> comments = new ArrayList<>();
        Cursor cursor = database.query(TABLE_COMMENTS, allColumns, COMMENTS_ROUTE_ID + " = " + id, null,null,null,null,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return comments;
    }

    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId(cursor.getLong(0));
        comment.setSubject(cursor.getString(1));
        comment.setContent(cursor.getString(2));
        comment.setRoute_id(cursor.getLong(5));
        return comment;
    }
}
