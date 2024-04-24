package service;

public class BoardDTO {
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTeamLeader() {
		return teamLeader;
	}
	public void setTeamLeader(int i) {
		this.teamLeader = i;
	}
	private int id;
    private String name;
    private int teamLeader;

    // Getters and setters
}