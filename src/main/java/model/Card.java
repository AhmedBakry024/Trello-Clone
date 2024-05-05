package model;

import java.util.ArrayList;
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
	    TO_DO,
	    IN_PROGRESS,
	    TEST,
	    DONE
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cardId;
	private String title;
	private String description;
	private Status status = Status.TO_DO;
	
	@Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> comments = new ArrayList<>();
	
	private int assignedTo;
	private int reporterId;
	
	
	@ManyToOne
	@JoinColumn(name ="list_id")
	@JsonIgnore
	private ListOfCards list;

	
	public Card(int reporterId) {
		this.reporterId = reporterId;
		this.creationDate = new Date();
	};
	
	public Card(int cardId,String Title,String description,int assignedTo) {
		this.cardId = cardId;
		this.title = Title;
		this.description = description;
		this.assignedTo = assignedTo;
	}
	
	public void setList(ListOfCards list) {
		this.list = list;
	}
	
	public ListOfCards getList() {
		return list !=null ? list : null;
	}
	
	public int getCardId() {
		return cardId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getComments() {
		return comments;
	}

	public void addComment(String Comment) {
		if(Comment != null && !Comment.trim().isEmpty()) {
			this.comments.add(Comment);
		}
	}

	public int getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(int assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public int getreporterId() {
		return reporterId;
	}

	
	public int getListId() {
		if(list == null)
			return 0;
		return list.getListId();
	}
	@ManyToOne
    private sprint sprint;
	public sprint getSprint() {
		return sprint;
	}

	public void setSprint(sprint sprint) {
		this.sprint = sprint;
	}
	
	public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
	
}