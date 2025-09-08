/** A class that represents a path via pursuit curves. */
public class Path {

    private Point curr;
    private Point next;
    // TODO
    public Path(double x, double y) {
        // more code goes here!
        curr = new Point(x, y);
        next = new Point(x, y);
    }

    public double getCurrX(){
        return curr.getX();
    }

    public double getCurrY(){
        return curr.getY();
    }

    public double getNextX(){
        return next.getX();
    }

    public double getNextY(){
        return next.getY();
    }

    public Point getCurrentPoint(){
        return curr;
    }

    public void setCurrentPoint(Point point){
        curr = point;
    }

    public void iterate(double dx, double dy){
        curr = next;
        next = new Point(next.getX() + dx, next.getY() + dy); // 更新 next
        // curr 移到上一次的 next
        }
}
