package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RealSystemPicture database table.
 * 
 */
@Entity
public class RealSystemPicture extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String name;

	//bi-directional many-to-one association to RealSystem
    @ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="RealSystem_id")
	private RealSystem realSystem;

    public RealSystemPicture() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RealSystem getRealSystem() {
		return this.realSystem;
	}

	public void setRealSystem(RealSystem realSystem) {
		this.realSystem = realSystem;
	}
	
}