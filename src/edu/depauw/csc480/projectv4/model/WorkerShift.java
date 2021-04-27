package edu.depauw.csc480.projectv4.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WORKER_SHIFT")
public class WorkerShift {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "WSId")
	private Long wsId;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "WorkerId")
	private Worker worker;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ShiftId")
	private Shift shift;
	
	protected WorkerShift() {
		// No-argument constructor for JPA
	}
	
	public WorkerShift(Worker worker, Shift shift) {
		this.worker = worker;
		this.shift = shift;
	}
	
	public Long getWsId() {
		return wsId;
	}
	
	public Worker getWorker() {
		return worker;
	}
	
	public Shift getShift() {
		return shift;
	}

}
