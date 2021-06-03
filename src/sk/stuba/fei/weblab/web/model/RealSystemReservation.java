package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Calendar;


/**
 * The persistent class for the RealSystemSchedule database table.
 * 
 */
@Entity
public class RealSystemReservation extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

    @Temporal( TemporalType.TIMESTAMP)
	private Calendar from;

    @Temporal( TemporalType.TIMESTAMP)
	private Calendar to;

	//bi-directional many-to-one association to RealSystem
    @ManyToOne
	@JoinColumn(name="RealSystem_id")
	private RealSystem realSystem;

	//bi-directional many-to-one association to User
    @ManyToOne
	@JoinColumn(name="User_id")
	private User user;

    public RealSystemReservation() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getFrom() {
		return this.from;
	}

	public void setFrom(Calendar from) {
		this.from = from;
	}

	public Calendar getTo() {
		return this.to;
	}

	public void setTo(Calendar to) {
		this.to = to;
	}

	public RealSystem getRealSystem() {
		return this.realSystem;
	}

	public void setRealSystem(RealSystem realSystem) {
		this.realSystem = realSystem;
	}
	
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}