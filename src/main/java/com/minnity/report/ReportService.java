package com.minnity.report;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ReportService {

  //task 1: Return number of requests that were made for each company. (e.g. companyId -> requestNumber)
  public Map<Integer, Long> calculateNumberOfRequestsPerCompany(List<RequestLog> requestLogs) {
    Map<Integer, Long> requestsPerCompany = new HashMap<>();

    requestLogs.stream().map(requestLog -> requestLog.getCompanyId())
                        .forEach(request -> {
      if(requestsPerCompany.containsKey(request)){
        Long numOfRequests = requestsPerCompany.get(request) + 1;
        requestsPerCompany.replace(request, numOfRequests);
      } else {
        requestsPerCompany.put(request, 1L);
      }
    });

    return requestsPerCompany;
  }

  //task 2: Count and return requests per company that finished with an error HTTP response code (>=400)
  // NOTE: the return type changed to Map<Integer, Long> (companyId -> count) as per the email conversation
  public Map<Integer, Long> findRequestsWithError(List<RequestLog> requestLogs) {
    Map<Integer, Long> requestsWithError = new HashMap();
    requestLogs.stream()
                .filter(requestLog -> requestLog.getRequestStatus()>=400)
                .forEach(requestLog -> {
                  Integer companyId = requestLog.getCompanyId();
                  if(requestsWithError.containsKey(companyId)){
                     Long count = requestsWithError.get(companyId) + 1;
                     requestsWithError.put(companyId, count);
                  } else {
                      requestsWithError.put(companyId, 1L);
                  }
                });
    return requestsWithError;
  }

  //task 3: find and print API (requests path) that on average takes the longest time to process the request.
  public String findRequestPathWithLongestDurationTime(List<RequestLog> requestLogs) {
      Map<String, List<Long>> processTimesPerRequest = getProcessTimesPerRequest(requestLogs);
      Map<String, Long> averageTimesPerRequest = getAverageTimesPerRequest(processTimesPerRequest);
      return getRequestPathWithLongestDurationTime(averageTimesPerRequest);
  }

  private Map<String, List<Long>> getProcessTimesPerRequest(List<RequestLog> requestLogs) {
      Map<String, List<Long>> processTimesPerRequest = new HashMap<>();

      requestLogs.forEach(requestLog -> {
          String requestPath = requestLog.getRequestPath();
          long processTime = requestLog.getRequestDuration();
          if(!processTimesPerRequest.containsKey(requestPath)){
              List<Long> listOfProcessTimes = new ArrayList<>();
              listOfProcessTimes.add(processTime);
              processTimesPerRequest.put(requestPath, listOfProcessTimes);
          } else {
              processTimesPerRequest.get(requestPath).add(processTime);
          }
      });

      return processTimesPerRequest;
  }

  private Map<String, Long> getAverageTimesPerRequest(Map<String, List<Long>> processTimesPerRequest) {
      Map<String, Long> averageTimesPerRequest = new HashMap<>();
      processTimesPerRequest.forEach((requestPath, requestDurationList) -> {
          long requestDurationSum =
                  requestDurationList.stream()
                                    .reduce(0L, (current, count) -> current + count);
          long averageTime = requestDurationSum/requestDurationList.size();
          averageTimesPerRequest.put(requestPath, averageTime);
      });

      return averageTimesPerRequest;
  }

  private String getRequestPathWithLongestDurationTime(Map<String, Long> averageTimesPerRequest) {
      long maxAverage = Long.MIN_VALUE;
      String requestPathWithLongestDurationTime = null;

      for (Map.Entry entry: averageTimesPerRequest.entrySet()) {
          String key = (String) entry.getKey();
          long currentAverage = (long) entry.getValue();
          if(currentAverage > maxAverage) {
              maxAverage = currentAverage;
              requestPathWithLongestDurationTime = key;
          }
      }
      return requestPathWithLongestDurationTime;
  }

}
