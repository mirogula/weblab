package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;

import sk.stuba.fei.weblab.paramproperties.ParamType;

import java.util.List;


/**
 * The persistent class for the Parameter database table.
 * 
 */
@Entity
public class Parameter extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String displayName;

	private int paramNumber;
	
	private String defaultValue;

	@Enumerated(EnumType.STRING)
	private ParamType paramType;

    @Lob()
	private String simulinkBlockName;

    @Lob()
	private String simulinkParamName;

	//bi-directional many-to-one association to EnumValue
	@OneToMany(mappedBy="parameter", cascade=CascadeType.ALL)
	private List<EnumValue> enumValues;

	//bi-directional many-to-one association to ParamGroup
    @ManyToOne
	@JoinColumn(name="ParamGroup_id")
	private ParamGroup paramGroup;

    public Parameter() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getParamNumber() {
		return this.paramNumber;
	}

	public void setParamNumber(int paramNumber) {
		this.paramNumber = paramNumber;
	}

	public ParamType getParamType() {
		return this.paramType;
	}

	public void setParamType(ParamType paramType) {
		this.paramType = paramType;
	}

	public String getSimulinkBlockName() {
		return this.simulinkBlockName;
	}

	public void setSimulinkBlockName(String simulinkBlockName) {
		this.simulinkBlockName = simulinkBlockName;
	}

	public String getSimulinkParamName() {
		return this.simulinkParamName;
	}

	public void setSimulinkParamName(String simulinkParamName) {
		this.simulinkParamName = simulinkParamName;
	}

	public List<EnumValue> getEnumValues() {
		return this.enumValues;
	}

	public void setEnumValues(List<EnumValue> enumValues) {
		this.enumValues = enumValues;
	}
	
	public ParamGroup getParamGroup() {
		return this.paramGroup;
	}

	public void setParamGroup(ParamGroup paramGroup) {
		this.paramGroup = paramGroup;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
}