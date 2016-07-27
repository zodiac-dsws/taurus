package br.cefetrj.sagitarii.persistence.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.cefetrj.sagitarii.core.types.ActivityStatus;
import br.cefetrj.sagitarii.core.types.ActivityType;

@Entity
@Table(name="activities", indexes = {
       @Index(columnList = "id_activity", name = "activity_hndx"),
       @Index(columnList = "tag", name = "activity_tag_hndx"),
       @Index(columnList = "serial", name = "activity_serial_hndx")
})     
public class Activity implements Comparable<Activity> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_activity")
	private int idActivity;	

	@Column(length=150)
	private String tag;

	@Column(length=8)
	private String serial;
	
	@Column(length=250)
	private String description;

	@Column(length=50)
	@Enumerated(EnumType.STRING)
	private ActivityType type;
	
	@Column(length=50)
	@Enumerated(EnumType.STRING)
	private ActivityStatus status;
	
	@Column(length=250)
	private String command;

	@Column(length=250)
	private String executorAlias;
	
	@ManyToOne
	@JoinColumn(name="id_fragment", foreignKey = @ForeignKey(name = "fk_activity_frag") )
	@Fetch(FetchMode.JOIN)
	private Fragment fragment;	

    @ManyToMany(cascade=CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name="relationship",
        joinColumns={@JoinColumn(name="id_activity")},
        inverseJoinColumns={@JoinColumn(name="id_relation")})
    private Set<Relation> inputRelations = new HashSet<Relation>();	
	
	@OneToOne
	@JoinColumn( foreignKey = @ForeignKey(name = "fk_activity_table") )
	private Relation outputRelation;	
	
    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="dependencies",
        joinColumns={@JoinColumn(name="id_master")},
        inverseJoinColumns={@JoinColumn(name="id_slave")})
    @OrderBy("tag ASC")
    private Set<Activity> nextActivities = new HashSet<Activity>();
 
    @ManyToMany(mappedBy="nextActivities", fetch = FetchType.EAGER)
    @OrderBy("tag ASC")
    private Set<Activity> previousActivities = new HashSet<Activity>();

    public void addInputRelation( Relation relation ) {
    	inputRelations.add( relation );
    }
    
    
	// Checa se a atividade em questao possui uma unica atividade tipo SPLIT antes dela
	public boolean previousIsSplit( ) {
		boolean result = false;
		if ( getPreviousActivities().size() == 1 ) {
			if ( getPreviousActivities().iterator().next().getType() == ActivityType.SPLIT_MAP ) {
				result = true;
			}
		}
		return result;
	}

    
    public Relation getInputRelation() {
    	return inputRelations.iterator().next();
    }
    
    public Activity() {
		status = ActivityStatus.READY;
        UUID uuid = UUID.randomUUID();
        serial = uuid.toString().toUpperCase().substring(0,8);
	}	

    public void addNextActivity( Activity activity ) {
    	nextActivities.add(activity);
    }

    public void addPreviousActivity( Activity activity ) {
    	previousActivities.add(activity);
    }
    
    
	public Set<Activity> getNextActivities() {
		return nextActivities;
	}

	public void setNextActivities(Set<Activity> nextActivities) {
		this.nextActivities = nextActivities;
	}

	public Set<Activity> getPreviousActivities() {
		return previousActivities;
	}

	public void setPreviousActivities(Set<Activity> previousActivities) {
		this.previousActivities = previousActivities;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public ActivityType getType() {
		return type;
	}

	public void setType(ActivityType type) {
		this.type = type;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public ActivityStatus getStatus() {
		return status;
	}

	public void setStatus(ActivityStatus status) {
		this.status = status;
	}

	public Set<Relation> getInputRelations() {
		return inputRelations;
	}

	public void setInputRelations(Set<Relation> inputRelations) {
		this.inputRelations = inputRelations;
	}

	public Relation getOutputRelation() {
		return outputRelation;
	}

	public void setOutputRelation(Relation outputRelation) {
		this.outputRelation = outputRelation;
	}

	public String getSerial() {
		return serial;
	}

	@Override
	public int compareTo(Activity o) {
		return this.getSerial().compareToIgnoreCase( o.getSerial() );
	}
	
	@Override
	public boolean equals(Object arg0) {
		 return this.getSerial().equalsIgnoreCase( ( (Activity)arg0).getSerial() );
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public String getExecutorAlias() {
		return executorAlias;
	}

	public void setExecutorAlias(String executorAlias) {
		this.executorAlias = executorAlias;
	}

}
