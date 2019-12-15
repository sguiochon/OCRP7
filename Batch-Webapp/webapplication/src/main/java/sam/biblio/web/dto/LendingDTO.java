package sam.biblio.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.dozer.Mapping;

import java.time.LocalDate;

public class LendingDTO {

    @JsonProperty("id")
    @Mapping("id")
    Long lendingId;
    @Mapping("start")
    LocalDate startDate;
    @Mapping("end")
    LocalDate endDate;

    private Boolean isExtendable;
    private Boolean isOverDue;
    private DocumentDTO document;

    public Boolean getOverDue() {
        return isOverDue;
    }

    public void setOverDue(Boolean overDue) {
        isOverDue = overDue;
    }

    public Long getLendingId() {
        return lendingId;
    }

    public void setLendingId(Long lendingId) {
        this.lendingId = lendingId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getExtendable() {
        return isExtendable;
    }

    public void setExtendable(Boolean extendable) {
        isExtendable = extendable;
    }

    public DocumentDTO getDocument() {
        return document;
    }

    public void setDocument(DocumentDTO document) {
        this.document = document;
    }
}
