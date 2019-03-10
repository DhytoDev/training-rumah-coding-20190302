package id.ac.unhas.rs.pasienku.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.ac.unhas.rs.pasienku.Patient;
import id.ac.unhas.rs.pasienku.R;

public class PatientListAdapter extends ArrayAdapter<Patient> {

    public PatientListAdapter(Context context, int resource, List<Patient> patients) {
        super(context, R.layout.list_patient_item, patients);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_patient_item, null);
        }

        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        TextView textViewGender = convertView.findViewById(R.id.text_view_gender);
        TextView textViewPayment = convertView.findViewById(R.id.text_view_payment);
        TextView textViewPhone = convertView.findViewById(R.id.text_view_phone);

        Patient patient = getItem(position);

        textViewName.setText(String.format("%s %s", patient.getFirstName(), patient.getLastName()));
        textViewGender.setText(patient.getGender());
        textViewPayment.setText(patient.getPaymentMethod());
        textViewPhone.setText(patient.getPhoneNumber());

        return convertView;
    }


}
