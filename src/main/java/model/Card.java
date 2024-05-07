package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> comments;
    @Column(name = "creation_date")
    private String creationDate;
    private int assignedTo;
    private String description;
    @JsonProperty("cardStatus")
    private String cardStatus;
    @JsonProperty("deedline")
    private String deedline;
    @ManyToOne
    @JoinColumn(name ="list_id")
    @JsonIgnore
    private ListOfCards list;
    private int reporterId;
    @ManyToOne
    private sprint sprint;
    private String title;

    public Card() {
    	this.creationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public Card(int cardId, String Title, String description, int assignedTo, int reporterId) {
        this.cardId = cardId;
        this.title = Title;
        this.description = description;
        this.assignedTo = assignedTo;
        this.reporterId = reporterId;
        this.creationDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void addComment(String Comment) {
        if (Comment != null && !Comment.trim().isEmpty()) {
            this.comments.add(Comment);
        }
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public int getCardId() {
        return cardId;
    }

    public List<String> getComments() {
        return comments;
    }


    public String getDescription() {
        return description;
    }

    public ListOfCards getList() {
        return list != null ? list : null;
    }

    public int getListId() {
        if (list == null)
            return 0;
        return list.getListId();
    }

    public int getreporterId() {
        return reporterId;
    }

    public sprint getSprint() {
        return sprint;
    }

    public String getTitle() {
        return title;
    }
    
    public String getCreationDate() {
        return creationDate;
    }
    
    public String getDeedline() {
        return deedline;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setList(ListOfCards list) {
        this.list = list;
    }

    public void setSprint(sprint sprint) {
        this.sprint = sprint;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setreporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public void setStatus(String Status) {
        this.cardStatus = Status;
    }
    
    public void setDeedline(String deedline) {
        this.deedline = deedline;
    }
}
