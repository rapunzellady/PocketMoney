package com.example.lauraleisner.pocketmoney;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class Account implements Serializable {

    /**
     * globales Konto
     */
    static Account globalAccount = new Account();

    /**
     * Eigenschaften des Kontos
     */
    public int amount = 0;
    public int i = 28;
    /**
     * eigener Integer amountTransfer für den selbst hinzugefügten oder gelöschten Betrag für den Kontostand
     * wenn dieser nicht extra dem Kontostand hinzugefügt wird,
     * ändert sich der Betrag amount für den globalAccount.
     */
    public int amountTransfer = 0;
    public GregorianCalendar start = new GregorianCalendar();

}
