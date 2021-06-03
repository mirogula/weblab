package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the InputPortNumber database table.
 * 
 */
@Entity
public class InputPortNumber extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private int dataArrayLength;

	private String portNumber;

	//bi-directional many-to-one association to Scope
    @ManyToOne
	@JoinColumn(name="Scope_id")
	private Scope scope;

    public InputPortNumber() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getDataArrayLength() {
		return this.dataArrayLength;
	}

	public void setDataArrayLength(int dataArrayLength) {
		this.dataArrayLength = dataArrayLength;
	}

	public String getPortNumber() {
		return this.portNumber;
	}

	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}

	public Scope getScope() {
		return this.scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
}