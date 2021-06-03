package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ParamGroup database table.
 * 
 */
@Entity
public class ParamGroup extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private int groupNumber;

	private String name;

	//bi-directional many-to-one association to SimulinkSchema
    @ManyToOne
	@JoinColumn(name="SimulinkSchema_id")
	private SimulinkSchema simulinkSchema;

	//bi-directional many-to-one association to Parameter
	@OneToMany(mappedBy="paramGroup", cascade=CascadeType.ALL)
	private List<Parameter> parameters;

    public ParamGroup() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getGroupNumber() {
		return this.groupNumber;
	}

	public void setGroupNumber(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SimulinkSchema getSimulinkSchema() {
		return this.simulinkSchema;
	}

	public void setSimulinkSchema(SimulinkSchema simulinkSchema) {
		this.simulinkSchema = simulinkSchema;
	}
	
	public List<Parameter> getParameters() {
		return this.parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}
	
}