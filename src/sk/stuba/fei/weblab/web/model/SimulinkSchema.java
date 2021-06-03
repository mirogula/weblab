package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Calendar;
import java.util.List;


/**
 * The persistent class for the SimulinkSchema database table.
 * 
 */
@Entity
public class SimulinkSchema extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

    @Temporal( TemporalType.TIMESTAMP)
	private Calendar additionDate;

	private String name;

	private String schemaPictureName;

	//bi-directional many-to-one association to ParamGroup
	@OneToMany(mappedBy="simulinkSchema", cascade=CascadeType.ALL)
	private List<ParamGroup> paramGroups;

	//bi-directional many-to-one association to Scope
	@OneToMany(mappedBy="simulinkSchema", cascade=CascadeType.ALL)
	private List<Scope> scopes;

	//bi-directional many-to-one association to RealSystem
    @ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="RealSystem_id")
	private RealSystem realSystem;

	//bi-directional many-to-one association to SimulinkSchemaLocalizedDescription
	@OneToMany(mappedBy="simulinkSchema", cascade=CascadeType.ALL)
	private List<SimulinkSchemaLocalizedDescription> simulinkSchemaLocalizedDescriptions;

    public SimulinkSchema() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getAdditionDate() {
		return this.additionDate;
	}

	public void setAdditionDate(Calendar additionDate) {
		this.additionDate = additionDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchemaPictureName() {
		return this.schemaPictureName;
	}

	public void setSchemaPictureName(String schemaPictureName) {
		this.schemaPictureName = schemaPictureName;
	}

	public List<ParamGroup> getParamGroups() {
		return this.paramGroups;
	}

	public void setParamGroups(List<ParamGroup> paramGroups) {
		this.paramGroups = paramGroups;
	}
	
	public List<Scope> getScopes() {
		return this.scopes;
	}

	public void setScopes(List<Scope> scopes) {
		this.scopes = scopes;
	}
	
	public RealSystem getRealSystem() {
		return this.realSystem;
	}

	public void setRealSystem(RealSystem realSystem) {
		this.realSystem = realSystem;
	}
	
	public List<SimulinkSchemaLocalizedDescription> getSimulinkSchemaLocalizedDescriptions() {
		return this.simulinkSchemaLocalizedDescriptions;
	}

	public void setSimulinkSchemaLocalizedDescriptions(List<SimulinkSchemaLocalizedDescription> simulinkSchemaLocalizedDescriptions) {
		this.simulinkSchemaLocalizedDescriptions = simulinkSchemaLocalizedDescriptions;
	}
	
}