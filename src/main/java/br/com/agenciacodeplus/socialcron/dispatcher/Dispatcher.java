package br.com.agenciacodeplus.socialcron.dispatcher;

import br.com.agenciacodeplus.socialcron.schedules.Schedule;
import br.com.agenciacodeplus.socialcron.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;

@Component
public class Dispatcher {

  public void sync(Schedule schedule) throws IOException  {
    Date now = new Date();
    Date scheduleDate = schedule.getDate();
    Date fiftenMinutesAfterNow = DateUtils.sumDate(now, Calendar.MINUTE, 15);
    
    if(scheduleDate.after(now) && scheduleDate.before(fiftenMinutesAfterNow)) {
      sendSync(schedule);
    }
  
  }
  
  public void sync(List<Schedule> schedules) throws IOException {
    for(Schedule schedule : schedules) {
      sync(schedule);
    }
  }
  
  private void sendSync(Schedule schedule) throws IOException {
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpPost request = new HttpPost("http://dispatcher.socialcron.com.br/sync");
    StringEntity params = new StringEntity(new ObjectMapper().writeValueAsString(schedule));
    request.addHeader("content-type", "application/json");
    request.setEntity(params);
    httpClient.execute(request);
  }
  
}
