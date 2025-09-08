public class TriangleDrawer2 {
    public static void drawTriangle() {
        for (int x=1; x <= 5; x+=1){
            for (int y=0; y < x; y += 1){
                System.out.print("*");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        drawTriangle();
    }

}
