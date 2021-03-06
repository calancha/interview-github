package com.orb.interview.model.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;
import java.lang.StringBuilder;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.sql.Timestamp;
public class Rate {
  private final String[] validCurrencies = {"USDJPY", "USDEUR",
                                            "EURUSD", "EURJPY",
                                            "JPYEUR", "JPYUSD"};

  private final String uriEndpoint = "https://forex.1forge.com/1.0.3/quotes";
  // You need to register an API key, see:
  // https://1forge.com/forex-data-api/api-documentation
  private final String apiKey = "PUT-YOUR-REGISTERED-API-KEY";

  /*
    A map with the rate and timestamp of the requested rates, for instance:
    {USDEUR={rate=0.855696, timestamp=1531616528},
     EURUSD={rate=1.1686393, timestamp=1531616528}}
  */  
  private HashMap<String, HashMap<String, Object>> rates;
  /*
    Use the cached value when: current-system-time - cached-timestamp < cacheTimeSeconds,
    otherwise perform a new http request.
  */
  private final Integer cacheTimeSeconds = 300;
  public Rate() { 
    this.rates = new HashMap<String, HashMap<String, Object>>();
  }

  public String reversedCurrencies(String currencies) {
    int len = currencies.length() / 2;
    return currencies.substring(len, 2*len) + currencies.substring(0, len);
  }

  public Long getTimestamp(String currencies) {
    return new Long(rates.get(currencies).get("timestamp").toString());
  }
 
  private Boolean useCacheValue(String currencies) {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    if (rates.isEmpty() || rates.get(currencies) == null) {
      return false;
    } else {
      return (timestamp.getTime() / 1000 - getTimestamp(currencies)) < cacheTimeSeconds;
    }
  }

  private Float getCachedRate(String currencies) {
    if (rates.get(currencies) == null) {
      return null;
    } else {
      return new Float(rates.get(currencies).get("rate").toString());
    }
  }

  // We retrieve the rate for currencies and the reversed exchange.
  // TODO: We could retrieve all the supported exchanges on each API call;
  // that might save many API calls.
  public Float getRate(String currencies) throws UnsupportedCurrencyException, Exception {
    if (!Arrays.asList(validCurrencies).contains(currencies)) {
      throw new UnsupportedCurrencyException();
    }
    Float rate = getCachedRate(currencies);
    if (useCacheValue(currencies) && rate != null) {
      update(currencies, rate, true);
      return rate;
    } else {
      StringBuilder result = new StringBuilder();
      String uri = uriEndpoint + "?pairs=" + currencies + "," + reversedCurrencies(currencies) + "&api_key=" + apiKey;
      URL url = new URL(uri);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = rd.readLine()) != null) {
        result.append(line);
      }
      rd.close();

      ObjectMapper mapper = new ObjectMapper();
      System.out.println(">>> result: " + result.toString());
      String jsonString = result.toString();
      HashMap[] jsonArr = mapper.readValue(jsonString, HashMap[].class);
      Float rate_reversed;
      rate = new Float(jsonArr[0].get("price").toString());
      rate_reversed = new Float(jsonArr[1].get("price").toString());

      update(currencies, rate, false);
      update(reversedCurrencies(currencies), rate_reversed, false);
      return rate;
    }
  }

  // Update the reversed exchange as well, i.e. USD -> JPY and JPY -> USD.
  private void update(String currencies, Float rate, Boolean useCache) throws ArithmeticException{
    HashMap<String, Object> currencyMap = new HashMap<String, Object>();
    currencyMap.put("rate", rate);
    if (useCache) {
      currencyMap.put("timestamp", getTimestamp(currencies));
    } else {
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      currencyMap.put("timestamp", timestamp.getTime() / 1000);
    }
    rates.put(currencies, currencyMap);
  }
}
