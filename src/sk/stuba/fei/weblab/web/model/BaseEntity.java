package sk.stuba.fei.weblab.web.model;

import java.io.Serializable;

public abstract class BaseEntity<T> implements Serializable {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract T getId();
 
    public abstract void setId(T id);
 
    @Override
    public int hashCode() {
        return (getId() != null)
            ? (getClass().hashCode() + getId().hashCode())
            : super.hashCode();
    }
 
    @SuppressWarnings("unchecked")
	@Override
    public boolean equals(Object other) {
        return (other != null && getClass() == other.getClass() && getId() != null)
            ? getId().equals(((BaseEntity<?>) other).getId())
            : (other == this);
    }

}
