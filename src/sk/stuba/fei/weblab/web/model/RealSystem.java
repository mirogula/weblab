package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Calendar;
import java.util.List;


/**
 * The persistent class for the RealSystem database table.
 * 
 */
@Entity
public class RealSystem extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

    @Temporal( TemporalType.TIMESTAMP)
	private Calendar additionDate;

	private String color;

	private String name;

	private String urlAddress;

	//bi-directional many-to-one association to RealSystemLocalizedDescription
	@OneToMany(mappedBy="realSystem", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<RealSystemLocalizedDescription> realSystemLocalizedDescriptions;

	//bi-directional many-to-one association to RealSystemPicture
	@OneToMany(mappedBy="realSystem", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<RealSystemPicture> realSystemPictures;

	//bi-directional many-to-one association to RealSystemSchedule
	@OneToMany(mappedBy="realSystem", cascade=CascadeType.ALL)
	private List<RealSystemReservation> realSystemSchedules;

	//bi-directional many-to-one association to SimulinkSchema
	@OneToMany(mappedBy="realSystem", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private List<SimulinkSchema> simulinkSchemas;

    public RealSystem() {
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

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrlAddress() {
		return this.urlAddress;
	}

	public void setUrlAddress(String urlAddress) {
		this.urlAddress = urlAddress;
	}

	public List<RealSystemLocalizedDescription> getRealSystemLocalizedDescriptions() {
		return this.realSystemLocalizedDescriptions;
	}

	public void setRealSystemLocalizedDescriptions(List<RealSystemLocalizedDescription> realSystemLocalizedDescriptions) {
		this.realSystemLocalizedDescriptions = realSystemLocalizedDescriptions;
	}
	
	public List<RealSystemPicture> getRealSystemPictures() {
		return this.realSystemPictures;
	}

	public void setRealSystemPictures(List<RealSystemPicture> realSystemPictures) {
		this.realSystemPictures = realSystemPictures;
	}
	
	public List<RealSystemReservation> getRealSystemSchedules() {
		return this.realSystemSchedules;
	}

	public void setRealSystemSchedules(List<RealSystemReservation> realSystemSchedules) {
		this.realSystemSchedules = realSystemSchedules;
	}
	
	public List<SimulinkSchema> getSimulinkSchemas() {
		return this.simulinkSchemas;
	}

	public void setSimulinkSchemas(List<SimulinkSchema> simulinkSchemas) {
		this.simulinkSchemas = simulinkSchemas;
	}
	
}