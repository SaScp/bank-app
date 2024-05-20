package ru.alex.testcasebankapp.util;

public final class Constant {
    public final static double DEFAULT_BALANCE = 50.0;

    public final static String SELECT_ALL_TRANSACTION = "SELECT * FROM bank_api.t_transaction ORDER BY created_at DESC";

    public final static String SELECT_TRANSACTION_BY_ID = "SELECT * FROM bank_api.t_transaction where id=?";

    public final static String SELECT_TRANSACTION_BY_FROM_USER_CARD_OR_TO_USER_CARD = "SELECT * FROM bank_api.t_transaction WHERE from_user_card=? or to_user_card=? ORDER BY created_at DESC";

    public final static String UPDATE_ALL_BALANCE_ON_FIVE_PROCENT = "UPDATE bank_api.t_account SET current_balance = current_balance + (current_balance * 0.05) WHERE current_balance < initial_deposit * 2.07";

    public final static String SELECT_CURRENT_BALANCE_BY_CARD = "SELECT current_balance FROM bank_api.t_account WHERE card = ?";

    public final static String UPDATE_BALANCE_BY_CARD = "UPDATE bank_api.t_account SET current_balance = ? WHERE card = ?";

    public final static String CREATE_INFO_ABOUT_TRANSACTION = "INSERT INTO bank_api.t_transaction (id, from_user_card, to_user_card, amount, created_at) VALUES (?, ?, ?, ?, ?)";
}
