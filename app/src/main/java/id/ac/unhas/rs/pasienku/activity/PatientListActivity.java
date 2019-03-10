package id.ac.unhas.rs.pasienku.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPatientList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_add_patient) {
            Intent intent = new Intent(PatientListActivity.this,
                    MainActivity.class);
            startActivity(intent);
        } else if(id == R.id.menu_delete_all) {

        }

        return super.onOptionsItemSelected(item);
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
