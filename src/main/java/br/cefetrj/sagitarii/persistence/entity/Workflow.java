package br.cefetrj.sagitarii.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.cefetrj.sagitarii.misc.ZipUtil;

@Entity
@Table(name="workflows", indexes = {
        @Index(columnList = "id_workflow", name = "wrkflw_id_hndx"),
        @Index(columnList = "tag", name = "wrkflw_tag_hndx")
})   
public class Workflow {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_workflow")
	private int idWorkflow;
	
	@Column(length=150, unique=true)
	private String tag;
	
	@Column(length=250)
	private String description;
	
	@Column( name="activities_specs")
	private byte[] activitiesSpecs;	

	@Column(columnDefinition = "TEXT", name="image_data")
	private String imagePreviewData;	
	
    @OneToMany(orphanRemoval=true,  mappedBy="workflow", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OrderBy("status ASC")
    private Set<Experiment> experiments;		
	
	@ManyToOne
	@JoinColumn(name="id_user")
	@Fetch(FetchMode.JOIN)
	private User owner;    
    
    public Workflow() {
    	experiments = new HashSet<Experiment>();
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

	public int getIdWorkflow() {
		return idWorkflow;
	}

	public void setIdWorkflow(int idWorkflow) {
		this.idWorkflow = idWorkflow;
	}

	public Set<Experiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	
}
