package pinda;

public class Snippet 
{

	private String name;
	private float x;
	private float y;
	private Object color;
	
	public Snippet(String name, float f, float g, Object color) 
	{
		super();
		this.name = name;
		this.x = f;
		this.y = g;
		this.color = color;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
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
