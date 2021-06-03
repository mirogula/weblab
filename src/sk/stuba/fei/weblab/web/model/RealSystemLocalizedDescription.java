package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the RealSystemLocalizedDescription database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "RealSystemLocalizedDescription.findByLocaleAndRealSystem",
        query = "SELECT d FROM RealSystemLocalizedDescription d WHERE d.locale = :locale AND d.realSystem = :realSystem")
})
public class RealSystemLocalizedDescription extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

    @Lob()
	private String description;

	//bi-directional many-to-one association to Locale
    @ManyToOne
	@JoinColumn(name="Locale_id")
	private Locale locale;

	//bi-directional many-to-one association to RealSystem
    @ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="RealSystem_id")
	private RealSystem realSystem;

    public RealSystemLocalizedDescription() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public RealSystem getRealSystem() {
		return this.realSystem;
	}

	public void setRealSystem(RealSystem realSystem) {
		this.realSystem = realSystem;
	}
	
}