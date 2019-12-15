package sam.biblio.model.library;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import sam.biblio.model.mappers.LocalDateDeserializer;
import sam.biblio.model.mappers.LocalDateSerializer;

import java.time.LocalDate;

public class Lending {

    @JsonProperty("lendingId")
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")//, shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = LocalDateDeserializer.class, as = LocalDate.class)
    @JsonSerialize(using = LocalDateSerializer.class, as = LocalDate.class)
    private LocalDate start;
    @JsonFormat(pattern = "yyyy-MM-dd")//, shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = LocalDateDeserializer.class, as = LocalDate.class)
    @JsonSerialize(using = LocalDateSerializer.class, as = LocalDate.class)
    private LocalDate end;
    private Integer nbPostponement;
    private Member member;
    private Copy copy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Integer getNbPostponement() {
        return nbPostponement;
    }

    public void setNbPostponement(Integer nbPostponement) {
        this.nbPostponement = nbPostponement;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

}
