package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Scope database table.
 * 
 */
@Entity
public class Scope extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

    @Lob()
	private String simulinkScopeName;

	//bi-directional many-to-one association to InputPortNumber
	@OneToMany(mappedBy="scope", cascade=CascadeType.ALL)
	private List<InputPortNumber> inputPortNumbers;

	//bi-directional many-to-one association to SimulinkSchema
    @ManyToOne
	@JoinColumn(name="SimulinkSchema_id")
	private SimulinkSchema simulinkSchema;

    public Scope() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSimulinkScopeName() {
		return this.simulinkScopeName;
	}

	public void setSimulinkScopeName(String simulinkScopeName) {
		this.simulinkScopeName = simulinkScopeName;
	}

	public List<InputPortNumber> getInputPortNumbers() {
		return this.inputPortNumbers;
	}

	public void setInputPortNumbers(List<InputPortNumber> inputPortNumbers) {
		this.inputPortNumbers = inputPortNumbers;
	}
	
	public SimulinkSchema getSimulinkSchema() {
		return this.simulinkSchema;
	}

	public void setSimulinkSchema(SimulinkSchema simulinkSchema) {
		this.simulinkSchema = simulinkSchema;
	}
	
}