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
    public int amountTransfer = 0;
    public GregorianCalendar start = new GregorianCalendar();

}
