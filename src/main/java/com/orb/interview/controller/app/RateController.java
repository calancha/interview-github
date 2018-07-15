package com.orb.interview.controller.app;

import java.util.HashMap;
import java.util.Collections;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orb.interview.model.app.Rate;

@RestController
public class RateController {

  private Rate rate = new Rate();

  @RequestMapping(value = "/rates", method = RequestMethod.GET, produces = "application/json")
  public Object exchange(@RequestParam(value="from", defaultValue="USD") String from,
                         @RequestParam(value="to", defaultValue="JPY") String to) throws Exception {
    String currencies = from + to;
    return Collections.singletonMap("rate", rate.getRate(currencies));
  }
}
