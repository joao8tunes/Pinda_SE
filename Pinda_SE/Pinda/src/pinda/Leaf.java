package pinda;

public class Leaf 
{
	
	private String name;
	private int value;
	private Object color;
	
	public Leaf(String name, int value, Object color) {
		super();
		this.name = name;
		this.value = value;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	public Object getColor()
	{
		return color;
	}
	
	public void setColor(Object color)
	{
		this.color = color;
	}

}
