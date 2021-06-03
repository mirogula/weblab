package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the Role database table.
 * 
 */
@Entity
@NamedQueries({
    @NamedQuery(
    		name = "Role.findByRoleName",
    		query = "SELECT r FROM Role r WHERE r.roleName = :roleName")
})
public class Role extends sk.stuba.fei.weblab.web.model.BaseEntity<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

    @Lob()
	private String roleDescription;

	private String roleName;

	//bi-directional many-to-many association to User
    @ManyToMany(mappedBy="roles")
	private List<User> users;

    public Role() {
    }

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRoleDescription() {
		return this.roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}