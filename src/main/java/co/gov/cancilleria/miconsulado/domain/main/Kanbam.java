package co.gov.cancilleria.miconsulado.domain.main;


import co.gov.cancilleria.miconsulado.domain.main.enumeration.State;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Kanbam.
 */
@Entity
@Table(name = "kanbam")
public class Kanbam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_size")
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @OneToMany(mappedBy = "kanbam")
    private Set<Employee> owners = new HashSet<>();

    @OneToMany(mappedBy = "kanbam")
    private Set<Task> tasks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSize() {
        return size;
    }

    public Kanbam size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public State getState() {
        return state;
    }

    public Kanbam state(State state) {
        this.state = state;
        return this;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Set<Employee> getOwners() {
        return owners;
    }

    public Kanbam owners(Set<Employee> employees) {
        this.owners = employees;
        return this;
    }

    public Kanbam addOwner(Employee employee) {
        this.owners.add(employee);
        employee.setKanbam(this);
        return this;
    }

    public Kanbam removeOwner(Employee employee) {
        this.owners.remove(employee);
        employee.setKanbam(null);
        return this;
    }

    public void setOwners(Set<Employee> employees) {
        this.owners = employees;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public Kanbam tasks(Set<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public Kanbam addTask(Task task) {
        this.tasks.add(task);
        task.setKanbam(this);
        return this;
    }

    public Kanbam removeTask(Task task) {
        this.tasks.remove(task);
        task.setKanbam(null);
        return this;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kanbam)) {
            return false;
        }
        return id != null && id.equals(((Kanbam) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Kanbam{" +
            "id=" + getId() +
            ", size=" + getSize() +
            ", state='" + getState() + "'" +
            "}";
    }
}
