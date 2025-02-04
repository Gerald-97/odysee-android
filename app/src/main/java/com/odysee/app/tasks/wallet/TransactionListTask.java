package com.odysee.app.tasks.wallet;

import android.os.AsyncTask;
import android.view.View;

import java.util.List;

import com.odysee.app.exceptions.ApiCallException;
import com.odysee.app.model.Transaction;
import com.odysee.app.utils.Helper;
import com.odysee.app.utils.Lbry;

public class TransactionListTask extends AsyncTask<Void, Void, List<Transaction>> {
    private final int page;
    private final int pageSize;
    private String authToken;
    private final View progressView;
    private final TransactionListHandler handler;
    private Exception error;

    public TransactionListTask(int page, int pageSize, View progressView, TransactionListHandler handler) {
        this.page = page;
        this.pageSize = pageSize;
        this.progressView = progressView;
        this.handler = handler;
    }

    public TransactionListTask(int page, int pageSize, String authToken, View progressView, TransactionListHandler handler) {
        this(page, pageSize, progressView, handler);
        this.authToken = authToken;
    }

    protected void onPreExecute() {
        Helper.setViewVisibility(progressView, View.VISIBLE);
    }
    protected List<Transaction> doInBackground(Void... params) {
        List<Transaction> transactions = null;
        try {
            if (authToken != null)
                transactions = Lbry.transactionList(page, pageSize, authToken);
            else
                transactions = Lbry.transactionList(page, pageSize, "");
        } catch (ApiCallException ex) {
            error = ex;
        }

        return transactions;
    }

    protected void onPostExecute(List<Transaction> transactions) {
        Helper.setViewVisibility(progressView, View.GONE);
        if (handler != null) {
            if (transactions != null) {
                handler.onSuccess(transactions, transactions.size() < pageSize);
            } else {
                handler.onError(error);
            }
        }
    }

    public interface TransactionListHandler {
        void onSuccess(List<Transaction> transactions, boolean hasReachedEnd);
        void onError(Exception error);
    }
}
