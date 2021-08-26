package by.prus.LabProject.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name ="role")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 5879723455135817255L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 20)
    private String name;

    @ManyToMany(mappedBy = "rolesOfUser")
    @JsonBackReference
    private Collection<UserEntity> users;

    //Persist - если Юзер будет удален, нам не нужно удалять роль, т.к. роль принадлежит разным юзерам.
    // Eager - когда UserDetails будет считан из базы данных, роли будут загружены сразу же. Это нужно для аутентификации
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(
            name ="role_authority",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name ="authority_id", referencedColumnName = "id")
    )
    private Collection<AuthorityEntity> authoritiesOfRole;

    public RoleEntity(){}
    public RoleEntity(String name) { this.name=name; }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Collection<UserEntity> getUsers() { return users; }
    public void setUsers(Collection<UserEntity> users) { this.users = users; }
    public Collection<AuthorityEntity> getAuthorities() { return authoritiesOfRole; }
    public void setAuthorities(Collection<AuthorityEntity> authorities) { this.authoritiesOfRole = authorities; }


}
