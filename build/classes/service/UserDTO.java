package Project;


public class UserDTO   {
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isTeamLeader() {
		return isTeamLeader;
	}
	public void setTeamLeader(boolean isTeamLeader) {
		this.isTeamLeader = isTeamLeader;
	}
	private int id;
    private String name;
    private String email;
    private boolean isTeamLeader;

    // Getters and setters
}
