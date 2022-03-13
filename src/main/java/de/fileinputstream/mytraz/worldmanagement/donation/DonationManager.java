package de.fileinputstream.mytraz.worldmanagement.donation;

import com.paypal.api.payments.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 09.08.2020
 * © 2020 Alexander Fiedler
 **/
public class DonationManager {

    public void createDonationLink(final String playername, final String amountString) {
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal("10.00");

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent(playername + " VIP Rang");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("https://mc-survival.de/dcancel");
        redirectUrls.setReturnUrl("https://mc-survival.de/dsuccess");
        payment.setRedirectUrls(redirectUrls);
    }
}
