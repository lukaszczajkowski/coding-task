package com.minnity.report;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class ReportServiceTest {

  ReportService reportService;

  @Test
  public void calculateNumberOfRequestsPerCompanyTest() {
      reportService = new ReportService();
      List<RequestLog> requestLogs = new ArrayList<>();

      //Case: the requestLogs list is empty

      Map<Integer, Long> expectedMap = new HashMap<>();
      Map<Integer, Long> actualMap =
              reportService.calculateNumberOfRequestsPerCompany(requestLogs);

      assertEquals(expectedMap, actualMap);
      assertEquals(expectedMap.size(), actualMap.size());

      //Case: the requestLogs list contains 3 different company ids

      for (int i = 0; i < 3; i++) {
        RequestLog requestLog = RequestLog.RequestLogBuilder
                .aRequestLog()
                .withCompanyId(i)
                .build();
        requestLogs.add(requestLog);
      }
      actualMap = reportService.calculateNumberOfRequestsPerCompany(requestLogs);
      expectedMap = new HashMap<>();

      for(RequestLog log : requestLogs) {
        expectedMap.put(log.getCompanyId(), 1L);
      }

      assertEquals(expectedMap, actualMap);
      assertEquals(expectedMap.size(), actualMap.size());

      //Case: the requestLog list contains 4 company ids, 1 is duplicated

      RequestLog requestLog1 = RequestLog.RequestLogBuilder
              .aRequestLog()
              .withCompanyId(2)
              .build();
      requestLogs.add(requestLog1);
      expectedMap.put(2, 2L);
      actualMap = reportService.calculateNumberOfRequestsPerCompany(requestLogs);

      assertEquals(expectedMap, actualMap);
      assertEquals(expectedMap.size(), actualMap.size());
  }

  @Test
  public void findRequestPathWithLongestDurationTimeTest() {
      reportService = new ReportService();
      List<RequestLog> requestLogs = prepareRequestLogList(9);
      String actual = reportService.findRequestPathWithLongestDurationTime(requestLogs);
      String expected = "/vivaldi";

      assertEquals(expected, actual);
  }


  private RequestLog prepareRequestLog(String endpoint, long requestDuration) {
    return RequestLog.RequestLogBuilder.aRequestLog()
            .withRequestPath(endpoint)
            .withRequestDuration(requestDuration)
            .build();
  }

  private List<RequestLog> prepareRequestLogList(int numOfLogs) {
      List<RequestLog> requestLogs = new ArrayList<>();
      RequestLog requestLog = null;
      String endpoint = null;

      for(int i = 0; i < numOfLogs; i++) {
          long requestDuration = i * 100;
          if(i < 3) {
              endpoint = "/ping";
          } else if (i >= 3 && i < 6) {
              endpoint = "/users";
          } else {
              endpoint = "/vivaldi";
          }
          requestLog = prepareRequestLog(endpoint, requestDuration);
          requestLogs.add(requestLog);
      }

      return requestLogs;
  }

}