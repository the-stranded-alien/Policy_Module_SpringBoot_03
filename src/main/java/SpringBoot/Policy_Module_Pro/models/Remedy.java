package SpringBoot.Policy_Module_Pro.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Remedy {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Activity activity;

    private LocalDateTime remedyTime;
    private String remedyType;
    private Boolean status;

    public Remedy() {

    }

    public Remedy(LocalDateTime remedyTime, String remedyType, Boolean status) {
        this.remedyTime = remedyTime;
        this.remedyType = remedyType;
        this.status = status;
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

    public LocalDateTime getRemedyTime() {
        return remedyTime;
    }

    public void setRemedyTime(LocalDateTime remedyTime) {
        this.remedyTime = remedyTime;
    }

    public String getRemedyType() {
        return remedyType;
    }

    public void setRemedyType(String remedyType) {
        this.remedyType = remedyType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Remedy)) return false;
        Remedy other = (Remedy) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Remedy{" +
                "remedyTime=" + remedyTime +
                ", remedyType='" + remedyType + '\'' +
                ", status=" + status +
                '}';
    }
}

