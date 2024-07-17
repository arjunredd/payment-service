package com.avanse.jpa.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.avanse.jpa.model.PaymentStatusResponse;

@Repository
public class MstCustomRepository {

//	@Autowired
//	private SessionFactory sessionFactory;

	@Autowired
	private DataSource dataSource;

	public List<PaymentStatusResponse> getData(String query2) {
		List<PaymentStatusResponse> paymentStatusResponses = new ArrayList<PaymentStatusResponse>();
		Connection connection = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			resultSet = connection.createStatement().executeQuery(query2);
			while (resultSet.next()) {
				addElement(paymentStatusResponses, resultSet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (resultSet != null) {
					resultSet.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
			}

		}
		return paymentStatusResponses;
	}

	private void addElement(List<PaymentStatusResponse> paymentStatusResponses, ResultSet resultSet)
			throws SQLException {
		PaymentStatusResponse paymentResponse = new PaymentStatusResponse();
		paymentResponse.setLoanNumber(resultSet.getString("LANumber"));
		paymentResponse.setRequestPurpose(resultSet.getString("RequestPurpose"));
		paymentResponse.setSourceApplicationName(resultSet.getString("SourceApplicationName"));
		paymentResponse.setPaymentRequestID(resultSet.getString("PaymentRequestID"));
		paymentResponse.setLoanType(resultSet.getString("LoanType"));
		paymentResponse.setAmount(resultSet.getDouble("Amount"));
		paymentResponse.setRequestStatus(resultSet.getString("RequestStatus"));
		paymentResponse.setCurrency(resultSet.getString("Currency"));
		paymentResponse.setPaymentPurpose(resultSet.getString("PaymentPurpose"));
		paymentResponse.setMerchantID(resultSet.getString("MerchantID"));
		paymentResponse.setRequestDate(resultSet.getDate("PaymentRequestDateTime"));
		paymentResponse.setMode(resultSet.getString("Mode"));
		paymentResponse.setRefTransaction(resultSet.getString("RefTransaction"));
		paymentResponse.setPaymentRef(resultSet.getString("paymentRef"));
		paymentResponse.setBankCode(resultSet.getString("bankCode"));
		paymentResponse.setFavourName(resultSet.getString("favourName"));
		paymentResponse.setRemark(resultSet.getString("remarks"));
		paymentResponse.setOrderId(resultSet.getString("razorpay_order_id"));
		paymentResponse.setPaymentId(resultSet.getString("razorpay_payment_id"));
		paymentResponse.setFundingAccount(resultSet.getString("FundingAccount"));
		paymentResponse.setPennantReceiptNo(resultSet.getString("PennantReceiptNo"));
		paymentResponse.setReceiptDate(resultSet.getDate("PennantReceiptDate"));
		paymentResponse.setReceiptError(resultSet.getString("PennantErrorDesc"));
		paymentResponse.setAuthCode(resultSet.getString("auth_code"));
		paymentResponse.setPaymentRequestError(resultSet.getString("PaymentRequestError"));
		paymentResponse.setWebhookError(resultSet.getString("PaymentRequestError"));
		paymentResponse.setAccountId(resultSet.getString("account_id"));
		paymentResponse.setSMSStatus(resultSet.getString("SMSStatus"));
		paymentResponse.setEmailStatus(resultSet.getString("EmailStatus"));
		paymentStatusResponses.add(paymentResponse);
	}
}
