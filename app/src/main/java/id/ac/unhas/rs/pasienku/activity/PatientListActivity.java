package id.ac.unhas.rs.pasienku.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.widget.ListView;

import java.util.List;

import id.ac.unhas.rs.pasienku.Patient;
import id.ac.unhas.rs.pasienku.R;
import id.ac.unhas.rs.pasienku.adapter.PatientListAdapter;
import id.ac.unhas.rs.pasienku.database.DatabaseManager;

public class PatientListActivity extends AppCompatActivity {

    private ListView patientListView;
    private SearchView searchView;

    private List<Patient> patients;
    private PatientListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        patientListView = findViewById(R.id.list_view_patient);
        searchView = findViewById(R.id.search_view);

        getPatientList();

    }

    private void getPatientList() {
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();

        patients = databaseManager.getAllPatients();

        adapter = new PatientListAdapter(this, patients);
        patientListView.setAdapter(adapter);

        databaseManager.close();
    }
}
