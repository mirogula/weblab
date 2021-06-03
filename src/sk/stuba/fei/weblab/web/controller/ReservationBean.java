package sk.stuba.fei.weblab.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import sk.stuba.fei.weblab.web.model.RealSystem;
import sk.stuba.fei.weblab.web.model.RealSystemReservation;
import sk.stuba.fei.weblab.web.service.RealSystemReservationService;
import sk.stuba.fei.weblab.web.service.RealSystemService;
import sk.stuba.fei.weblab.web.service.UserService;

@Named
@RequestScoped
public class ReservationBean {

	private ScheduleModel reservationsModel;

	private ScheduleEvent reservationEvent = new DefaultScheduleEvent();
	
	private Map<String, RealSystem> realSystems;

	private RealSystemReservation newReservation;
	
	private Date selectedDate;
	
	private Map<String, String> timeValues;
	
	private String time;
	
	private Map<String, Integer> durations;
	
	private Integer duration;
	
	@EJB
	private RealSystemService rsService;
	
	@EJB
	private UserService userService;
	
	@EJB
	private RealSystemReservationService rsrService;
	
	@PostConstruct
	public void init() {
		try {
			List<RealSystem> realSystemsList = rsService.findAll();
			realSystems = new HashMap<>();
			for (RealSystem realSystem : realSystemsList) {
				realSystems.put(realSystem.getName(), realSystem);
			}
			
			timeValues = new LinkedHashMap<>();
			for (int i=0; i<24; i++) {
				timeValues.put(i+":00", i+":00");
				timeValues.put(i+":30", i+":30");
			}
			
			durations = new HashMap<>();
			durations.put("30", 30);
			durations.put("60", 60);
			
			newReservation = new RealSystemReservation();
			
			//TODO fill reservationsModel with reservations from database
			reservationsModel = new DefaultScheduleModel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void submit() {
		try {
			newReservation.setUser(userService.findUserByUsername((String)SecurityUtils.getSubject().getPrincipal()));
			Calendar from = Calendar.getInstance();
			from.setTime(selectedDate);
			String hourMInute[] = time.split(":");
			from.set(Calendar.HOUR, Integer.parseInt(hourMInute[0]));
			from.set(Calendar.MINUTE, Integer.parseInt(hourMInute[1]));
			Calendar to = (Calendar)from.clone();
			to.add(Calendar.MINUTE, duration);
			newReservation.setFrom(from);
			newReservation.setTo(to);
			rsrService.create(newReservation);
			reservationsModel.addEvent(new DefaultScheduleEvent(newReservation.getRealSystem().getName(),from.getTime(), to.getTime()));
			newReservation = new RealSystemReservation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onDateSelect(SelectEvent selectEvent) {
		try {
		selectedDate = (Date)selectEvent.getObject();
		System.out.println(selectEvent.getObject().getClass().getName());
		System.out.println(selectedDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, RealSystem> getRealSystems() {
		return realSystems;
	}

	public void setRealSystems(Map<String, RealSystem> realSystems) {
		this.realSystems = realSystems;
	}

	public RealSystemReservation getNewReservation() {
		return newReservation;
	}

	public void setNewReservation(RealSystemReservation newReservation) {
		this.newReservation = newReservation;
	}

	public ScheduleEvent getEvent() {
		return reservationEvent;
	}


	public void setEvent(ScheduleEvent event) {
		this.reservationEvent = event;
	}


	public Date getSelectedDate() {
		return selectedDate;
	}


	public void setSelectedDate(Date selectedDate) {
		this.selectedDate = selectedDate;
	}


	public Map<String, Integer> getDurations() {
		return durations;
	}


	public void setDurations(Map<String, Integer> durations) {
		this.durations = durations;
	}


	public Integer getDuration() {
		return duration;
	}


	public void setDuration(Integer duration) {
		this.duration = duration;
	}


	public Map<String, String> getTimeValues() {
		return timeValues;
	}


	public void setTimeValues(Map<String, String> timeValues) {
		this.timeValues = timeValues;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}
}
