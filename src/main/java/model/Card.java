package model;


import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "card")
public class Card {
	enum Status {
	    DONE,
	    IN_PROGRESS,
	    TEST,
	    TO_DO
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cardId;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> comments ;
	@Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
	private int assignedTo;
	private String description;
	
	@ManyToOne
	@JoinColumn(name ="list_id")
	@JsonIgnore
	private ListOfCards list;
	
	private int reporterId;
	@ManyToOne
    private sprint sprint;
	
	private Status status = Status.TO_DO;
	
	private String title;
	
	public Card() {}
	
	public Card(int reporterId) {
		this.reporterId = reporterId;
		this.creationDate = new Date();
	};
	
	public Card(int cardId,String Title,String description,int assignedTo) {
		this.cardId = cardId;
		this.title = Title;
		this.description = description;
		this.assignedTo = assignedTo;
		this.creationDate = new Date();
	}
	
	public void addComment(String Comment) {
		if(Comment != null && !Comment.trim().isEmpty()) {
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

	public Date getCreationDate() {
        return creationDate;
    }

	public String getDescription() {
		return description;
	}

	public ListOfCards getList() {
		return list !=null ? list : null;
	}

	public int getListId() {
		if(list == null)
			return 0;
		return list.getListId();
	}

	public int getreporterId() {
		return reporterId;
	}

	public sprint getSprint() {
		return sprint;
	}

	public Status getStatus() {
        return status;
    }
	
	public String getTitle() {
		return title;
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

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void setTitle(String title) {
		this.title = title;
	}
    public void setreporterId(int reporterId) {
        this.reporterId = reporterId;
    } 
}