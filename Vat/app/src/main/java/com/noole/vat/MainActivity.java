package com.noole.vat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.noole.vat.R;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {
    //muutujate deklaareerimine
    private EditText prices, units;
    private TextView vat, exVat, inVat;
    private ConstraintLayout mainView;
    private Spinner Spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Objektile viitamine
        prices = findViewById(R.id.etPrice);
        units = findViewById(R.id.etNumUnits);
        vat = findViewById(R.id.txtVat);
        exVat = findViewById(R.id.txtExVat);
        inVat = findViewById(R.id.txtInVat);
        mainView = findViewById(R.id.mainView);

        Spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this, R.array.Country_Array, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner.setAdapter(countryAdapter);

        RadioGroup VatGroup = findViewById(R.id.radioGroup);
        VatGroup.setOnCheckedChangeListener((group, checkId) -> {
            RadioButton checkedRadioBtn = group.findViewById(checkId);
            if(checkedRadioBtn.isChecked()) {
                onCalculate(null);
            }
        });
    }

    public void onCalculate(View view) {
        try {
            String PriceTxt = prices.getText().toString();
            double price = PriceTxt.isEmpty() ? 1 : Double.parseDouble(PriceTxt);

            String UnitTxt = units.getText().toString();
            double unit = UnitTxt.isEmpty() ? 1 : Double.parseDouble(UnitTxt);

            if(price > 0 && unit > 0) {
                double CountryVatRate = 0.2;
                try {
                    CountryVatRate = Double.parseDouble(getResources().getStringArray(R.array.Country_vat_array)[Spinner.getSelectedItemPosition()]);
                } catch(NumberFormatException e) {
                    e.getStackTrace();
                }

                double exAmount = price * unit;
                double VAT = exAmount * CountryVatRate;
                double inAmount = exAmount + VAT;

                RadioButton rbtnExvat = (RadioButton) findViewById(R.id.rbtnExvat);
                if(rbtnExvat.isChecked()) {
                    VAT = exAmount * 0.2;
                    inAmount = exAmount;
                    exAmount -= VAT;
                }

                vat.setText(NumberFormat.getInstance().format(VAT) + "€");
                exVat.setText(NumberFormat.getInstance().format(exAmount) + "€");
                inVat.setText(NumberFormat.getInstance().format(inAmount) + "€");
            }
        } catch(IllegalArgumentException exception) {
            exception.printStackTrace();
            displayExceptionMessage(exception.getMessage());
        }
    }

    private void displayExceptionMessage(String message) {
        Snackbar snackbar = Snackbar.make(mainView, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void onClear(View view){
        prices.setText("");
        units.setText("");
        vat.setText("");
        inVat.setText("");
        exVat.setText("");
        prices.requestFocus();
    }
}