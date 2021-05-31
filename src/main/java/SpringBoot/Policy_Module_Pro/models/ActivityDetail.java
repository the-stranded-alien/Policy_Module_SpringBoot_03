package SpringBoot.Policy_Module_Pro.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class ActivityDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Policy policy;

    private String policyResult;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "activity_risk_checked",
            joinColumns = @JoinColumn(name = "activity_detail_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "risk_id", referencedColumnName = "id")
    )
    private Set<Risk> risksCheckedAgainst = new HashSet<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "activity_risk_violated",
            joinColumns = @JoinColumn(name = "activity_detail_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "risk_id", referencedColumnName = "id")
    )
    private Set<Risk> risksViolated = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "activity_risk_not_violated",
            joinColumns = @JoinColumn(name = "activity_detail_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "risk_id", referencedColumnName = "id")
    )
    private Set<Risk> risksNotViolated = new HashSet<>();

    public ActivityDetail() {

    }

    public ActivityDetail(String policyResult) {
        this.policyResult = policyResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getPolicyResult() {
        return policyResult;
    }

    public void setPolicyResult(String policyResult) {
        this.policyResult = policyResult;
    }

    public Set<Risk> getRisksCheckedAgainst() {
        return risksCheckedAgainst;
    }

    public void setRisksCheckedAgainst(Set<Risk> risksCheckedAgainst) {
        this.risksCheckedAgainst = risksCheckedAgainst;
    }

    public Set<Risk> getRisksViolated() {
        return risksViolated;
    }

    public void setRisksViolated(Set<Risk> risksViolated) {
        this.risksViolated = risksViolated;
    }

    public Set<Risk> getRisksNotViolated() {
        return risksNotViolated;
    }

    public void setRisksNotViolated(Set<Risk> risksNotViolated) {
        this.risksNotViolated = risksNotViolated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityDetail)) return false;
        ActivityDetail other = (ActivityDetail) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ActivityDetail{" +
                ", policyResult='" + policyResult + '\'' +
                '}';
    }
}

