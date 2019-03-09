package id.ac.unhas.rs.pasienku.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import id.ac.unhas.rs.pasienku.Patient;

public class DatabaseManager {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public long addPatient(Patient patient) {
        ContentValues cv = new ContentValues();
        cv.put("first_name", patient.getFirstName());
        cv.put("last_name", patient.getLastName());
        cv.put("phone", patient.getPhoneNumber());
        cv.put("gender", patient.getGender());
        cv.put("email", patient.getEmail());
        cv.put("date_of_birth", patient.getDateOfBirth());
        cv.put("payment_method", patient.getPaymentMethod());
        cv.put("assurance", patient.getAssurance());

        return db.insert("table_patient", null, cv);
    }

    public List<Patient> getAllPatients() {
        String sql = "SELECT * FROM table_patient ORDER BY first_name DESC";
        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();

        List<Patient> patients = new ArrayList<>();

        while (!cursor.isAfterLast()) {
            Patient patient = new Patient();
            patient.setFirstName(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
            patient.setLastName(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
            patient.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            patient.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            patient.setDateOfBirth(cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth")));
            patient.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow("payment_method")));
            patient.setAssurance(cursor.getString(cursor.getColumnIndexOrThrow("assurance")));
            patient.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            patients.add(patient);
            cursor.moveToNext();
        }

        cursor.close();

        return patients;
    }

}
