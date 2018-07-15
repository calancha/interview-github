package com.orb.interview.model.app;

import org.springframework.http.HttpStatus;

import com.orb.interview.model.BusinessException;

public class UnsupportedCurrencyException extends BusinessException {
  public static final String UNSUPPORTED_CURRENCY_KEY = "com.orb.interview.user.rates.Invalid";

  public UnsupportedCurrencyException() {
    super(HttpStatus.UNAUTHORIZED, UNSUPPORTED_CURRENCY_KEY);
  }
}
