package todo.model;

public class ToDoItem {
    private String id;
    private String description;
    private String dueDate;
    private boolean complete = false;
    private String toDoListTitle;

    public ToDoItem() {
    }

    public ToDoItem(String id, String description, String dueDate, boolean complete, String toDoListTitle) {
        this.id = id;
        this.description = description;
        this.dueDate = dueDate;
        this.complete = complete;
        this.toDoListTitle = toDoListTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getToDoListTitle() {
        return toDoListTitle;
    }

    public void setToDoListTitle(String toDoListTitle) {
        this.toDoListTitle = toDoListTitle;
    }
}
