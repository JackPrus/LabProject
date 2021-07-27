package by.prus.LabProject.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag implements Serializable {

    private static final long serialVerisonUID = 2342353453245L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

//    @OneToMany(mappedBy = "gift_certificate", fetch = FetchType.LAZY)
//    Set<GiftCertificate> certificates;


    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
