package edu.depauw.csc480.projectv4.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SHIFT")
public class Shift {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "SId")
	private Long sId;
	
	@Basic(optional = false)
	@Column(name = "startTime")
	private LocalTime startTime;
	
	@Basic(optional = false)
	@Column(name = "endTime")
	private LocalTime endTime;
	
	@Basic(optional = false)
	@Column(name = "date")
	private LocalDate date;
	
	@OneToMany(mappedBy = "shift")
	private Collection<WorkerShift> workerShifts;
	
	protected Shift() {
		// No-argument constructor for JPA
	}
	
	public Shift(LocalTime startTime, LocalTime endTime, LocalDate date) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.date = date;
	}
	
	public Long getSId() {
		return sId;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public LocalTime getEndTime() {
		return endTime;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public Collection<WorkerShift> getWorkershifts() {
		return workerShifts;
	}

}
