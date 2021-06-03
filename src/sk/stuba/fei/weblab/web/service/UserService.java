package sk.stuba.fei.weblab.web.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.User;


@Stateless
public class UserService extends GenericDataAccessService<User> {
       
    public UserService() {
        super(User.class);
    }
    
    public User findUserByUsername(String username) {
    	Map<String, Object> queryParamsMap = new HashMap<>();
    	queryParamsMap.put("username", username);
    	List<User> users = (List<User>)findWithNamedQuery("User.findByUsername", queryParamsMap, 1);
    	if(users.isEmpty()) {
    		return null;
    	} else {
    		return users.get(0);
    	}
    }
}
