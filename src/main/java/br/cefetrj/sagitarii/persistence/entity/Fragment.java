package br.cefetrj.sagitarii.persistence.entity;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

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

import br.cefetrj.sagitarii.core.types.ExecutionModel;
import br.cefetrj.sagitarii.core.types.FragmentStatus;

@Entity
@Table(name="fragments", indexes = {
        @Index(columnList = "id_fragment", name = "frag_id_hndx"),
        @Index(columnList = "serial", name = "frag_serial_hndx")
}) 
public class Fragment  {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_fragment")
	private int idFragment;		
	
	@Column(length=8)
	private String serial;
	
	@Column(length=50)
	@Enumerated(EnumType.STRING)
	private ExecutionModel type;
	
	@Column(length=50)
	@Enumerated(EnumType.STRING)
	private FragmentStatus status = FragmentStatus.PREVIEW;
	
	@Column(name="index_order")
	private Integer indexOrder = 0;

	@Column(name="remaining_instances")
	private Integer remainingInstances = 0;

	@Column(name="total_instances")
	private Integer totalInstances = 0;
	
    @OneToMany(orphanRemoval=true,  mappedBy="fragment", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OrderBy("tag ASC")
    private Set<Activity> activities;
    
	@Transient
	private Experiment experiment;
	
	@Column(name="id_experiment")
	private int idExperiment;	
	
	public ExecutionModel getType() {
		return type;
	}
	
	public Integer getIndexOrder() {
		return indexOrder;
	}

	public void setIndexOrder(Integer indexOrder) {
		this.indexOrder = indexOrder;
	}

	public void removeActivity( String serial ) {
		for ( Activity act : activities ) {
			if ( act.getSerial().equals( serial ) ) {
				activities.remove(act);
				act.setFragment(null);
				break;
			}
		}
	}
	
	public void addActivity( Activity activity ) {
		activity.setFragment(this);
		activities.add(activity);
	}
	
	public void setType( ExecutionModel type ) {
		this.type = type;
	}
	
	public Fragment() {
		activities = new TreeSet<Activity>();
        UUID uuid = UUID.randomUUID();
        serial = uuid.toString().toUpperCase().substring(0,8);
	}
	

	public void setActivities( Set<Activity> activities ) {
		for ( Activity act : activities ) {
			act.setFragment(this);
			this.activities.add( act );
		}
	}
	
	public Set<Activity> getActivities() {
		return activities;
	}
	
	@Override
	public boolean equals(Object arg0) {
		 return this.getSerial().equalsIgnoreCase( ((Fragment)arg0).getSerial() );
	}

	public int getIdFragment() {
		return idFragment;
	}

	public void setIdFragment(int idFragment) {
		this.idFragment = idFragment;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
		if ( experiment != null ) {
			this.idExperiment = experiment.getIdExperiment();
		}
	}

	public int getIdExperiment() {
		return idExperiment;
	}

	public void setIdExperiment(int idExperiment) {
		this.idExperiment = idExperiment;
	}

	public FragmentStatus getStatus() {
		return status;
	}

	public void setStatus(FragmentStatus status) {
		this.status = status;
	}

	public Integer getRemainingInstances() {
		return remainingInstances;
	}

	public void setRemainingInstances(Integer remainingInstances) {
		this.remainingInstances = remainingInstances;
	}

	public Integer getTotalInstances() {
		return totalInstances;
	}

	public void setTotalInstances(Integer totalInstances) {
		this.totalInstances = totalInstances;
	}

	
}
