public class TriangleDrawer {
    public static void drawTriangle() {
        int x = 0;
        while (x < 5){
            x += 1;
            int y = 0;
            while (y < x){
                y += 1;
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        drawTriangle();
    }

}
