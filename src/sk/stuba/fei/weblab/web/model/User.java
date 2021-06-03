package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.bval.constraints.Email;

import java.util.Calendar;
import java.util.List;


/**
 * The persistent class for the User database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "User.findByUsernameAndPassword",
        query = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password"),
    @NamedQuery(
    		name = "User.findByUsername",
    		query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Column(unique=true)
	@Size(max=50)
	private String username;
	
	@Size(max=256)
	private String password;
	
	@Size(max=100)
	@Email
	private String email;
	
    @ManyToOne
	@JoinColumn(name="Locale_id")
	private Locale locale;

    @Temporal( TemporalType.TIMESTAMP)
	private Calendar signupDate;

	//bi-directional many-to-one association to RealSystemSchedule
	@OneToMany(mappedBy="user")
	private List<RealSystemReservation> realSystemSchedules;

	//bi-directional many-to-many association to Role
	@ManyToMany
	@JoinTable(
			name="UserRoles"
			, joinColumns={
				@JoinColumn(name="userId")
				}
			, inverseJoinColumns={
				@JoinColumn(name="roleId")
				}
			)
	private List<Role> roles;

    public User() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Calendar getSignupDate() {
		return this.signupDate;
	}

	public void setSignupDate(Calendar signupDate) {
		this.signupDate = signupDate;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<RealSystemReservation> getRealSystemSchedules() {
		return this.realSystemSchedules;
	}

	public void setRealSystemSchedules(List<RealSystemReservation> realSystemSchedules) {
		this.realSystemSchedules = realSystemSchedules;
	}
	
	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
}