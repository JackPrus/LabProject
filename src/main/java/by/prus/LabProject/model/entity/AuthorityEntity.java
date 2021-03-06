package by.prus.LabProject.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "authority")
@EqualsAndHashCode
public class AuthorityEntity implements Serializable {

    private static final long serialVerisonUID = 2342355453245L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "authoritiesOfRole")
    @JsonBackReference
    Collection<RoleEntity> roles;

    public AuthorityEntity(){}
    public AuthorityEntity(String name) { this.name = name; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Collection<RoleEntity> getRoles() { return roles; }
    public void setRoles(Collection<RoleEntity> roles) { this.roles = roles; }


}
