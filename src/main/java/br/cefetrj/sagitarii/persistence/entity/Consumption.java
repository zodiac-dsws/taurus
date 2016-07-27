package br.cefetrj.sagitarii.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="consumptions", indexes = {
        @Index(columnList = "id_consumption,id_row,id_table,id_instance", name = "consumption_hndx")
})    
public class Consumption {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_consumption")
	private int idConsumption;
	
	@Column(name="id_row")
	private int idRow;
	
	@Column(name="id_table")
	private int idTable;

	@Column(name="id_activity")
	private int idActivity;
	
	@ManyToOne
	@JoinColumn(name="id_instance", foreignKey = @ForeignKey(name = "fk_consumption_instance"))
	@Fetch(FetchMode.JOIN)
	private Instance instance;	

	public int getIdConsumption() {
		return idConsumption;
	}

	public void setIdConsumption(int idConsumption) {
		this.idConsumption = idConsumption;
	}

	public int getIdRow() {
		return idRow;
	}

	public void setIdRow(int idRow) {
		this.idRow = idRow;
	}

	public int getIdTable() {
		return idTable;
	}

	public void setIdTable(int idTable) {
		this.idTable = idTable;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}
	
	public int getIdActivity() {
		return idActivity;
	}
}
