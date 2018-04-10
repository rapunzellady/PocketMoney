package com.example.lauraleisner.pocketmoney;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;




public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Anzeige vorbereiten
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * gespeichertes Konto laden
         */
        loadAccount();

        /**
         * Konto anzeigen
         */
        showAccount();
        showButtonPlus();
        showButtonMinus();
        showBalance();
        fee();
    }

    @Override
    protected void onPause() {

        /**
         * Konto speichern, wenn App pausiert/geschlossen wird
         */
        super.onPause();
        saveAccount();
    }



    /**
     * OnClickEvents
     */

    /**
     * wenn Button gedruekt, Betrag um i erhöhen, Kontostand und Geldabfrage aktualisieren
     * @param view payButton
     */
    public void pay(View view) {
        Account.globalAccount.amount += Account.globalAccount.i;
        showAccount();
        showBalance();
        fee();
    }

    /**
     * wenn Button gedruekt, i um 1 erhöhen
     * @param view plusButton
     */
    public void plus(View view) {
        Account.globalAccount.i += 1;
        showButtonPlus();
    }

    /**
     * wenn Button gedruekt, i um 1 verringern
     * @param view minusButton
     */
    public void minus(View view) {
        Account.globalAccount.i -= 1;
        showButtonMinus();
    }

    /**
     * fuegt den im MoneyEditText Textfeld eingegebenen Betrag dem Kontostand hinzu,
     * nicht aber dem globalAccount amount
     * @param view  addMoneyButton
     */
    public void addMoney(View view)  {
        EditText MoneyEditText = findViewById(R.id.MoneyEditText);
        String add = MoneyEditText.getText().toString();
        int transfer = Integer.parseInt(add);
        Account.globalAccount.amountTransfer += transfer;
        showBalance();
    }

    /**
     *  loescht den im MoneyEditText Textfeld eingegebenen Betrag vom Kontostand,
     *  nicht aber vom globalAccount amount
     * @param view substractMoneyButton
     */
    public void substractMoney(View view) {
        EditText MoneyEditText = findViewById(R.id.MoneyEditText);
        String add = MoneyEditText.getText().toString();
        int transfer = Integer.parseInt(add);
        Account.globalAccount.amountTransfer -= transfer;
        showBalance();
    }



    /**
     * Konto laden
     * wenn Fehler auftauchen, diese ausgleichen
     * (beim ersten laden ist bspw. keine Datei vorhanden, deshalb wird eine neues Konto angelegt
     * und i auf 28 gesetzt)
     */
    private void loadAccount() {
        try (ObjectInputStream stream = new ObjectInputStream(openFileInput("account.dat"))) {
            Account.globalAccount = (Account)stream.readObject();
            Account.globalAccount.i = stream.readInt();
            Account.globalAccount.amountTransfer = stream.readInt();
        }
        catch (FileNotFoundException e) {
            Account.globalAccount = new Account();
            Account.globalAccount.i = 28;
            Account.globalAccount.amountTransfer = 0;

        }
        catch (ClassNotFoundException e) {
            Account.globalAccount = new Account();
            Account.globalAccount.i = 28;
            Account.globalAccount.amountTransfer = 0;
        }
        catch (IOException e) {
            Account.globalAccount = new Account();
            Account.globalAccount.i = 28;
            Account.globalAccount.amountTransfer = 0;
        }
    }

    /**
     * Konto speichern
     * "Kontobetrag" und i auslesen, beide in die Datei account.dat schreiben
     */
    private void saveAccount() {
        try (ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("account.dat", MODE_PRIVATE))) {
            stream.writeObject(Account.globalAccount);
            stream.writeInt(Account.globalAccount.i);
            stream.writeInt(Account.globalAccount.amountTransfer);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Anzeigen
     */

    /**
     * anzeigen/aktualisieren, was im Textfeld amountView steht
     * bei Start oder Aenderung des Betrags
     */
    private void showAccount() {
        TextView amountView = findViewById(R.id.amountView);
        amountView.setText(String.valueOf(Account.globalAccount.i * getMonths() - Account.globalAccount.amount));
    }

    /**
     * anzeigen/aktualisieren, was auf payButton steht
     * bei Start oder Beruehrung des plusButtons
     */
    private void showButtonPlus() {
        Button payButton = findViewById(R.id.payButton);
        payButton.setText(String.valueOf("+" + Account.globalAccount.i));
    }

    /**
     * anzeigen/aktualisieren, was auf payButton steht
     * bei Start oder Beruehrung des minusButtons
     */
    private void showButtonMinus() {
        Button payButton = findViewById(R.id.payButton);
        payButton.setText(String.valueOf("+" + Account.globalAccount.i));
    }

    /**
     * Kontostand anzeigen/aktualisieren
     */
    private void showBalance() {
        TextView balanceView = findViewById(R.id.balanceView);
        int accountAmount = Account.globalAccount.amount + Account.globalAccount.amountTransfer;
        balanceView.setText(String.valueOf("aktueller Kontostand: " + accountAmount + "\u20ac"));
    }

    /**
     * Textfeld dueView gibt an, ob Geld für einen oder mehrere Monate fehlt,
     * ob alles bezahlt worden ist, oder sogar ein Vorschuss vorliegt
     */
    @SuppressLint("SetTextI18n")
    private void fee() {
        // Geld fehlt
        if (((Account.globalAccount.i * getMonths()) - Account.globalAccount.amount) > 0) {
            TextView dueView = findViewById(R.id.dueView);
            dueView.setText("Dir steht noch Geld von " + getMonths() + "Monat/en zu");
        }
        // Geldsumme stimmt
        else if (((Account.globalAccount.i * getMonths()) - Account.globalAccount.amount) == 0) {
            TextView dueView = findViewById(R.id.dueView);
            dueView.setText("Du hast alles Geld bekommen");
        }
        // Vorschuss
        else {
            TextView dueView = findViewById(R.id.dueView);
            dueView.setText("Du hast schon für zusätzliche Monate Geld bekommen");
        }
    }



    /**
     * ausrechnen, wie viele Monate Start her ist um damit dann den Betrag
     * der ausgezahlt werden muss auszurechnen
     * @return Anzahl Monate die bezahlt werden müssen
     */
    private int getMonths() {
        GregorianCalendar now = new GregorianCalendar();
        int startYear = Account.globalAccount.start.get(Calendar.YEAR);
        int startMonth = Account.globalAccount.start.get(Calendar.MONTH);
        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH);
        int years = currentYear - startYear;
        return currentMonth - startMonth + years * 12;
    }
}
