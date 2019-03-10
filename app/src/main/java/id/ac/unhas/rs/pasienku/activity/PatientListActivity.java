package id.ac.unhas.rs.pasienku.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        registerForContextMenu(patientListView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String keyword) {
                patients.clear();

                DatabaseManager databaseManager = new DatabaseManager(PatientListActivity.this);
                databaseManager.open();

                patients.addAll(databaseManager.searchPatientByName(keyword));
                adapter.notifyDataSetChanged();

                databaseManager.close();
                return false;
            }
        });

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

        if (id == R.id.menu_add_patient) {
            Intent intent = new Intent(PatientListActivity.this,
                    MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_delete_all) {
            new AlertDialog.Builder(this)
                    .setTitle("Hapus Semua Data")
                    .setMessage("Anda yakin ingin menghapus ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteAllPatients();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final int position = menuInfo.position;

        final Patient patient = patients.get(position);

        switch (id) {
            case R.id.update_menu:
                Intent intent = new Intent(PatientListActivity.this, MainActivity.class);
                intent.putExtra("patient_data", patient);
                startActivity(intent);

                break;
            case R.id.delete_menu:
                new AlertDialog.Builder(this)
                        .setTitle("Hapus Data")
                        .setMessage("Anda yakin ingin menghapus data ? ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseManager databaseManager = new DatabaseManager(PatientListActivity.this);
                                databaseManager.open();
                                databaseManager.deletePatientById(patient.getId());
                                databaseManager.close();;

                                patients.remove(position);

                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                break;
        }


        return super.onContextItemSelected(item);
    }

    private void getPatientList() {
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();

        patients = databaseManager.getAllPatients();

        adapter = new PatientListAdapter(this, patients);
        patientListView.setAdapter(adapter);

        databaseManager.close();
    }

    private void deleteAllPatients() {
        DatabaseManager databaseManager = new DatabaseManager(this);
        databaseManager.open();

        databaseManager.deleteAllPatients();

        databaseManager.close();
        patients.clear();
        adapter.notifyDataSetChanged();
    }
}
