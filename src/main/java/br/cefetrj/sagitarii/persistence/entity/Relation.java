package br.cefetrj.sagitarii.persistence.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name="tables", indexes = {
        @Index(columnList = "id_table", name = "table_id_hndx"),
        @Index(columnList = "name", name = "table_name_hndx")
})  
public class Relation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_table")
	private int idTable;	

	@Column(length=150)
	private String name;
	
	@Column(length=250)
	private String description;
	
	@Column(columnDefinition = "TEXT")
	private String schema;

    @OneToMany(orphanRemoval=true,  mappedBy="table", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OrderBy("domainName ASC")
    private Set<Domain> domains;		

	@Transient
	private boolean isEmpty = true;
	
	@Transient
	private int size;
	
	public Relation() {
		domains = new LinkedHashSet<Domain>();
	}
	
	public int getIdTable() {
		return idTable;
	}

	public void setIdTable(int idTable) {
		this.idTable = idTable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public boolean getIsEmpty() {
		return isEmpty;
	}

	public void setIsEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.isEmpty = ( size == 0 );
		this.size = size;
	}

	public Set<Domain> getDomains() {
		return domains;
	}

	public void setDomains(Set<Domain> domains) {
		this.domains = domains;
	}


    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(name).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Relation) ) return false;
       if (obj == this) return true;

        Relation rhs = (Relation) obj;
        return new EqualsBuilder().append(name, rhs.name).isEquals();
    }	
	
	
	

}
