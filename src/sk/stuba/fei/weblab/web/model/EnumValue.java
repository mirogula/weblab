package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the EnumValues database table.
 * 
 */
@Entity
@Table(name="EnumValues")
public class EnumValue extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

    @Lob()
	private String value;

	//bi-directional many-to-one association to Parameter
    @ManyToOne
	@JoinColumn(name="Parameter_id")
	private Parameter parameter;

    public EnumValue() {
    }

    @Override
	public Integer getId() {
		return this.id;
	}
    
    @Override
	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Parameter getParameter() {
		return this.parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}
	
}