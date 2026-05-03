package com.example.network.ktor

object PayPalConstant {
    const val BASE_URL = "https://api-m.sandbox.paypal.com"
    const val CLIENT_ID = "AZFbNHKqkWmWlXo2jGGgAyn3ukjW7TN7rv0TQsq2Nm3Q71Q2vpriSVjd8Dey1ZCTydVzsoQQ0CeeKf_r"
    const val SECRET_ID = "EMmR3sR6np9BtBapWQ4ZCP9ZgbsaetGFKPCb-J1zquw2movs742jTJdgmaqBBeZIG4-JKH3vIuKfTlWH"
    const val AUTH_KEY = "$CLIENT_ID:$SECRET_ID"

    const val RETURN_URL = "com.karim.ecommerce://paypalpay?success=true"
    const val CANCEL_URL = "com.karim.ecommerce://paypalpay?cancel=true"
}