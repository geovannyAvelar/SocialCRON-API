package br.com.agenciacodeplus.socialcron.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import br.com.agenciacodeplus.socialcron.periods.Period;
 
@Component
public class EventsValidator implements Validator {
 
  @Override
  public boolean supports(Class<?> clazz) {
    return clazz.isAssignableFrom(Event.class);
  }
 
  @Override
  public void validate(Object target, Errors errors) {
    if (!(target instanceof Event)) { throw new IllegalArgumentException(); }
 
    Event dto = (Event) target;
 
    if (dto.getLimitDate() != null) {
 
      // Initial date should be before limit date
      if (dto.getInitialDate().after(dto.getLimitDate())) {
        errors.rejectValue("initialDate", "event.limit_date_in_past");
      }
 
      if (dto.getInterval() <= 0) {
        errors.rejectValue("interval", "event.interval_zero");
      }
      
      if(dto.getPeriod().equals(Period.MINUTE) && dto.getInterval() < 20) {
        errors.rejectValue("interval", "event.interval_too_short");
      }
 
    }
 
  }
 
}
