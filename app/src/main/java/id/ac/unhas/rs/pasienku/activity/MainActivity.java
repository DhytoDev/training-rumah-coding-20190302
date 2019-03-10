package id.ac.unhas.rs.pasienku.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import id.ac.unhas.rs.pasienku.Patient;
import id.ac.unhas.rs.pasienku.R;
import id.ac.unhas.rs.pasienku.database.DatabaseManager;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button buttonSave;
    private EditText editTextFirstName, editTextLastName, editTextEmail,
            editTextPhone, editTextDateOfBirth;
    private RadioGroup radioGroupGender;
    private Spinner spinnerPayment;
    private CheckBox checkBoxBPJS, checkBoxInsurance;

    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        editTextFirstName = findViewById(R.id.edit_text_first_name);
        editTextLastName = findViewById(R.id.edit_text_last_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPhone = findViewById(R.id.edit_text_phone);
        editTextDateOfBirth = findViewById(R.id.edit_text_date_of_birth);
        radioGroupGender = findViewById(R.id.radio_gender);
        spinnerPayment = findViewById(R.id.spinner_payment_method);
        checkBoxBPJS = findViewById(R.id.checkbox_bpjs);
        checkBoxInsurance = findViewById(R.id.checkbox_insurance);

        buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPatient();
                finish();
            }
        });

        editTextDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                new DatePickerDialog(MainActivity.this,
                        MainActivity.this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseManager.close();
    }

    private void addPatient() {
        boolean isValid = validateForm();

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();
        String dateOfBirth = editTextDateOfBirth.getText().toString();
        int checkedGenderId = radioGroupGender.getCheckedRadioButtonId();
        String paymentMethod = spinnerPayment.getSelectedItem().toString();

        String gender = null;

        if (checkedGenderId == R.id.radio_man) {
            gender = "Pria";
        } else if (checkedGenderId == R.id.radio_woman) {
            gender = "Wanita";
        } else {
            return;
        }

        String assurance = "";

        if (checkBoxBPJS.isChecked()) {
            assurance += "BPJS, ";
        }

        if (checkBoxInsurance.isChecked()) {
            assurance += "Asuransi";
        }

        Patient patient = new Patient();
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setEmail(email);
        patient.setPhoneNumber(phone);
        patient.setDateOfBirth(dateOfBirth);
        patient.setGender(gender);
        patient.setPaymentMethod(paymentMethod);
        patient.setAssurance(assurance);

        if (!isValid) {
            validateForm();
        } else {
            long id = databaseManager.addPatient(patient);

            if (id == -1) {
                Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Data berhasil disimpan dengan id " + id, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean validateForm() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = editTextEmail.getText().toString();

        if (firstName.isEmpty()) {
            editTextFirstName.setError("Nama depan harus diisi");
            return false;
        }

        if (lastName.isEmpty()) {
            editTextLastName.setError("Nama belakang harus diisi");
            return false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email harus diisi");
            return false;
        }

        if (!isValidEmail(email)) {
            editTextEmail.setError("Invalid format email");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        editTextDateOfBirth.setText(dayOfMonth + "/" + month + "/" + year);
    }
}
