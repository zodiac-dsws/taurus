package br.cefetrj.sagitarii.persistence.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import br.cefetrj.sagitarii.core.types.ExperimentStatus;
import br.cefetrj.sagitarii.misc.ZipUtil;

@Entity
@Table(name="experiments", indexes = {
        @Index(columnList = "id_experiment", name = "exp_id_hndx"),
        @Index(columnList = "tagexec", name = "exp_tag_hndx")
}) 
public class Experiment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_experiment")
	private int idExperiment;

	@Column(length=150, name="tagexec")
	private String tagExec;
	
	@Column(length=50)
	@Enumerated(EnumType.STRING)
	private ExperimentStatus status;

	@Column(name="activities_specs")
	private byte[] activitiesSpecs;	

	@Column(columnDefinition = "TEXT", name="image_data")
	private String imagePreviewData;	
	
	@Column
	private String description;	
	
	@ManyToOne
	@JoinColumn(name="id_workflow", foreignKey = @ForeignKey(name = "fk_exp_wf"))
	@Fetch(FetchMode.JOIN)
	private Workflow workflow;    

	@ManyToOne
	@JoinColumn(name="id_user", foreignKey = @ForeignKey(name = "fk_exp_user"))
	@Fetch(FetchMode.JOIN)
	private User owner;    
	
	@Column
	@Type(type="timestamp")
	private Date creationDate;
	
	@Column
	@Type(type="timestamp")
	private Date lastExecutionDate;

	@Transient
	private String elapsedTime;

	@Column(length=15, name="serial_time")
	private String serialTime;
	
	@Column(length=15, name="lazy_time")
	private String lazyTime;

	@Column(length=15, name="real_time")
	private String realTime;

	@Column
	@Type(type="timestamp")
	private Date finishDateTime;
	
	@Column
	private Boolean availability = false;
	
	@Column
	@Type(type="timestamp")
	private Date alterationDate;

	@Column
	private Double speedUp = 0.0;
	
	@Column
	private Double parallelEfficiency = 0.0;

	@Column
	private Double coresWorking = 0.0;
	
    public Experiment() {
        UUID uuid = UUID.randomUUID();
        tagExec = uuid.toString().toUpperCase().substring(0,17);
		status = ExperimentStatus.STOPPED;
		setCreationDate( Calendar.getInstance().getTime() );
	}
    
    @Transient
    private List<Fragment> fragments = new ArrayList<Fragment>();
    
    @Transient
    public List<Relation> getUsedTables() {
    	List<Relation> tables = new ArrayList<Relation>();
    	for ( Fragment frag : fragments ) {
    		for ( Activity act : frag.getActivities() ) {
    			for( Relation rel : act.getInputRelations()  ) {
    				if ( !tables.contains( rel ) ) {
    					tables.add( rel );
    				}
    			}
    			if ( act.getOutputRelation() != null ) {
    				if ( !tables.contains(act.getOutputRelation() ) ) {
    					tables.add( act.getOutputRelation() );
    				}
    			}
    		}
    		
    	}
    	return tables;
    }
    
    
	public int getIdExperiment() {
		return idExperiment;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setIdExperiment(int idExperiment) {
		this.idExperiment = idExperiment;
	}

	public String getTagExec() {
		return tagExec;
	}

	public ExperimentStatus getStatus() {
		return status;
	}

	public void setStatus(ExperimentStatus status) {
		this.status = status;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastExecutionDate() {
		return lastExecutionDate;
	}

	public void setLastExecutionDate(Date lastExecutionDate) {
		this.lastExecutionDate = lastExecutionDate;
	}

	public Date getAlterationDate() {
		return alterationDate;
	}

	public void setAlterationDate(Date alterationDate) {
		this.alterationDate = alterationDate;
	}

	public String getActivitiesSpecs() {
		if ( activitiesSpecs != null ) {
			return ZipUtil.decompress( activitiesSpecs );
		} else {
			return "";
		}
	}

	public void setActivitiesSpecs(String activitiesSpecs) {
		this.activitiesSpecs = ZipUtil.compress( activitiesSpecs );
	}

	public String getImagePreviewData() {
		return imagePreviewData;
	}

	public void setImagePreviewData(String imagePreviewData) {
		this.imagePreviewData = imagePreviewData;
	}

	public List<Fragment> getFragments() {
		return fragments;
	}

	public void setFragments(List<Fragment> fragments) {
		for( Fragment frag : fragments ) {
			frag.setExperiment(this);
			frag.setIdExperiment( idExperiment );
		}
		this.fragments = fragments;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public Date getFinishDateTime() {
		return finishDateTime;
	}

	public void setFinishDateTime(Date finishDateTime) {
		this.finishDateTime = finishDateTime;
	}

	public Double getCoresWorking() {
		return coresWorking;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void updateMetrics() {
		/*
		ExperimentPerformance ep = new ExperimentPerformance( this );
		speedUp = ep.getSpeedUp();
		parallelEfficiency = ep.getParallelEfficiency();
		serialTime = ep.getSerialTime();
		elapsedTime = ep.getElapsedTime();
		lazyTime = ep.getLazyTime();
		realTime = ep.getRealTime();
		coresWorking = ep.getCoresWorking();
		*/
	}
	
	public String getElapsedTime() {
		return elapsedTime;
	}
	
	
	public String getLazyTime() {
		return lazyTime;
	}
	
	public void setLazyTime(String lazyTime) {
		this.lazyTime = lazyTime;
	}
	
	public String getSerialTime() {
		return serialTime;
	}
	
	public void setSerialTime(String serialTime) {
		this.serialTime = serialTime;
	}
	
	public String getRealTime() {
		return realTime;
	}
	
	public void setRealTime(String realTime) {
		this.realTime = realTime;
	}
	
	public Double getSpeedUp() {
		return speedUp;
	}
	
	public void setSpeedUp(Double speedUp) {
		this.speedUp = speedUp;
	}
	
	public Double getParallelEfficiency() {
		return parallelEfficiency;
	}
	
	public void setParallelEfficiency(Double parallelEfficiency) {
		this.parallelEfficiency = parallelEfficiency;
	}
	
}
