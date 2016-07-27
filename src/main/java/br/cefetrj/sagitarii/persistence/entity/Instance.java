package br.cefetrj.sagitarii.persistence.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;

import br.cefetrj.sagitarii.core.types.ActivityType;
import br.cefetrj.sagitarii.core.types.InstanceStatus;

@SuppressWarnings("serial")
@Entity
@Table(name="instances", indexes = {
        @Index(columnList = "id_instance", name = "instance_id_hndx"),
        @Index(columnList = "serial", name = "instance_serial_hndx")
}) 
public class Instance implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_instance")
	private Integer idInstance;
	
	@Column(name="start_date_time")
	@Type(type="timestamp")
	private Date startDateTime;

	@Column(name="finish_date_time")
	@Type(type="timestamp")
	private Date finishDateTime;

	@Column(name="id_fragment")
	private Integer idFragment;

	@Column( name = "cores_used")
	private Integer coresUsed = 0;

	@Column( name = "real_start_time_millis")
	private Long realStartTimeMillis = Long.valueOf(0);
	
	@Column( name = "cpu_cost")
	private Double cpuCost = 0.0;

	@Column( name = "real_finish_time_millis")
	private Long realFinishTimeMillis = Long.valueOf(0);

	@Column(length=50, name="executed_by")
	private String executedBy;	
	
	@Column(length=15)
	private String serial;

    @OneToMany(orphanRemoval=true,  mappedBy="instance", fetch = FetchType.LAZY)
    @OrderBy("id_table, id_row ASC")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Consumption> consumptions = new HashSet<Consumption>();
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@Column(name="qtd_activations")
	private Integer qtdActivations;

	@Column(name="times_tried")
	private Integer timesTried = 1;

	@Column(length=50)
	@Enumerated(EnumType.STRING)
	private InstanceStatus status;	
	
	@Column(length=50)
	@Enumerated(EnumType.STRING)
	private ActivityType type;	
	
	@Transient
	String finishedActivities;
	
	@Column(name="elapsed_time", length=50)
	private String elapsedTime = "000 00:00:00";

	@Column(name="elapsed_millis")
	private Long elapsedMillis = Long.valueOf(0);
	
	public void decreaseQtdActivations() {
		if ( qtdActivations > 0 ) {
			qtdActivations--;
		}
		if ( qtdActivations == 0 ) {
			status = InstanceStatus.FINISHED;
		}
	}

	public Instance() {
        UUID uuid = UUID.randomUUID();
        serial = uuid.toString().toUpperCase().substring(0,15);
        status = InstanceStatus.PIPELINED;
	}
	
	public void setCpuCost(Double cpuCost) {
		this.cpuCost = cpuCost;
	}
	
	public Double getCpuCost() {
		return cpuCost;
	}
	
	public void addConsumption( Consumption consumption) {
		consumption.setInstance( this );
		consumptions.add( consumption );
	}
	
	public void setQtdActivations(Integer qtdActivations) {
		this.qtdActivations = qtdActivations;
	}
	
	public Integer getQtdActivations() {
		return qtdActivations;
	}

	public Integer getIdInstance() {
		return idInstance;
	}

	public void setIdInstance(Integer idInstance) {
		this.idInstance = idInstance;
	}

	public Integer getIdFragment() {
		return idFragment;
	}

	public void setIdFragment(Integer idFragment) {
		this.idFragment = idFragment;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public String getFinishedActivities() {
		return finishedActivities;
	}

	public void setFinishedActivities(String finishedActivities) {
		this.finishedActivities = finishedActivities;
	}

	public InstanceStatus getStatus() {
		return status;
	}

	public void setStatus(InstanceStatus status) {
		this.status = status;
	}

	public Set<Consumption> getConsumptions() {
		return consumptions;
	}

	public void setConsumptions(Set<Consumption> consumptions) {
		for ( Consumption con : consumptions ) {
			con.setInstance( this );
		}
		this.consumptions = consumptions;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getFinishDateTime() {
		return finishDateTime;
	}

	public void setFinishDateTime(Date finishDateTime) {
		this.finishDateTime = finishDateTime;
		evaluateTime();
	}
	
	public void evaluateTime() {
		elapsedTime = millisToHumanTime( getElapsedMillis() );
	}
	
	private String millisToHumanTime( Long millis ) {
		String time = String.format("%03d %02d:%02d:%02d", 
				TimeUnit.MILLISECONDS.toDays( millis ),
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) -  
				TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
				TimeUnit.MILLISECONDS.toSeconds(millis) - 
				TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
		
		return time;
	}
	
	
	public Long getElapsedMillis() {
		if ( status == InstanceStatus.PIPELINED ) {
			elapsedMillis = Long.valueOf(0);
		} else {
			Date start = startDateTime;
			Date end = finishDateTime;
			if ( end == null ) {
				end = Calendar.getInstance().getTime();
			}
			elapsedMillis = end.getTime() - start.getTime();
			
			/*
			DateLibrary dl = DateLibrary.getInstance();
			dl.setTo( startDateTime );
			Calendar cl = Calendar.getInstance();
			
			if ( finishDateTime != null ) {
				cl.setTime( finishDateTime );
			} else {
				cl.setTime( Calendar.getInstance().getTime() );
			}
			
			elapsedMillis = dl.getDiffMillisTo( cl ) ;
			*/
		}
		return elapsedMillis;
	}	
	
	public String getRealElapsedTime() {
		Long realElapsedTime = realFinishTimeMillis - realStartTimeMillis;
		return millisToHumanTime( realElapsedTime );
	}
	
	public String getElapsedTime() {
		elapsedTime = millisToHumanTime( getElapsedMillis() );
		return elapsedTime;
	}
	
	public String getExecutedBy() {
		return executedBy;
	}
	
	public void setExecutedBy(String executedBy) {
		this.executedBy = executedBy;
	}
	
	public Integer getCoresUsed() {
		return coresUsed;
	}
	
	public void setCoresUsed(Integer coresUsed) {
		this.coresUsed = coresUsed;
	}
	
	public void setRealFinishTimeMillis(Long realFinishTimeMillis) {
		this.realFinishTimeMillis = realFinishTimeMillis;
	}
	
	public Long getRealFinishTimeMillis() {
		return realFinishTimeMillis;
	}
	
	public void setRealStartTimeMillis(Long realStartTimeMillis) {
		this.realStartTimeMillis = realStartTimeMillis;
	}
	
	public Long getRealStartTimeMillis() {
		return realStartTimeMillis;
	}
	
	public Integer getTimesTried() {
		return timesTried;
	}
	
	public void triedAgain() {
		this.timesTried++;
	}
	
}
