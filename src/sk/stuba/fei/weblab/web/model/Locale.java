package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the Locale database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(
    		name = "Locale.findByLanguageCode",
    		query = "SELECT l FROM Locale l WHERE l.languageCode = :languageCode")
})
public class Locale extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String languageCode;

	private String languageName;

	//bi-directional many-to-one association to RealSystemLocalizedDescription
	@OneToMany(mappedBy="locale")
	private List<RealSystemLocalizedDescription> realSystemLocalizedDescriptions;

	//bi-directional many-to-one association to SimulinkSchemaLocalizedDescription
	@OneToMany(mappedBy="locale")
	private List<SimulinkSchemaLocalizedDescription> simulinkSchemaLocalizedDescriptions;
	
	@OneToMany(mappedBy="locale")
	private List<User> users;

    public Locale() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLanguageCode() {
		return this.languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getLanguageName() {
		return this.languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public List<RealSystemLocalizedDescription> getRealSystemLocalizedDescriptions() {
		return this.realSystemLocalizedDescriptions;
	}

	public void setRealSystemLocalizedDescriptions(List<RealSystemLocalizedDescription> realSystemLocalizedDescriptions) {
		this.realSystemLocalizedDescriptions = realSystemLocalizedDescriptions;
	}
	
	public List<SimulinkSchemaLocalizedDescription> getSimulinkSchemaLocalizedDescriptions() {
		return this.simulinkSchemaLocalizedDescriptions;
	}

	public void setSimulinkSchemaLocalizedDescriptions(List<SimulinkSchemaLocalizedDescription> simulinkSchemaLocalizedDescriptions) {
		this.simulinkSchemaLocalizedDescriptions = simulinkSchemaLocalizedDescriptions;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}