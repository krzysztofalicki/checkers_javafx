import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.LinkedList;

public class Warcaby extends Application {

    StackPane root = new StackPane();
    Canvas canvas = new Canvas(800,800);
    GraphicsContext g = canvas.getGraphicsContext2D();

    public LinkedList<Rectangle> rectangles = new LinkedList<>();
    public int[][] table = {{0,1,0,1,0,1,0,1},{1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1},{1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1},{1,0,1,0,1,0,1,0},
            {0,1,0,1,0,1,0,1},{1,0,1,0,1,0,1,0}};
    public char[] rows = {'A','B','C','D','E','F','G','H'};
    public int[] columns = {1,2,3,4,5,6,7,8};

    public LinkedList<Pionek> pionki = new LinkedList<>();
    int aktywny = -1;

    public void checkAktywny(double x,double y) {
        int tmpx = (int) x/100;
        int tmpy = (int) y/100;
        int indeks = tmpy*8+tmpx;
        if(aktywny < 0) {
            if(pionki.get(indeks).widocznosc) {
                pionki.get(indeks).akt = true;
                aktywny = indeks;
            }
        }
        else {
            if(aktywny == indeks) {
                pionki.get(indeks).akt = false;
                aktywny = -1;
            }
            else {
                if(checkPole(tmpx,tmpy));
                else {
                    pionki.get(indeks).widocznosc=true;
                    pionki.get(indeks).rodzaj=pionki.get(aktywny).rodzaj;
                    pionki.get(aktywny).akt=false;
                    pionki.get(aktywny).widocznosc=false;
                    aktywny=-1;
                }
            }
        }
    }

    public boolean checkPole(int tmpx, int tmpy) {
        int indeks = tmpy*8+tmpx;
        double tmp = pionki.get(aktywny).x/100;
        int aktx = (int) tmp;
        tmp = pionki.get(aktywny).y/100;
        int akty = (int) tmp;
        if(pionki.get(indeks).widocznosc || table[tmpy][tmpx]==0) return true;

        if(pionki.get(aktywny).rodzaj==1) {
            if(tmpy<=akty || tmpy>=(akty+3)) return true;
            if(tmpy==(akty+1)) {
                if(tmpx==(aktx-1) || tmpx==(aktx+1)) return false;
                else return true;
            }
            if(tmpy==(akty+2)) {
                if(tmpx==(aktx-2)) {
                    if(pionki.get(aktywny+7).widocznosc==true && pionki.get(aktywny+7).rodzaj==2) {
                        pionki.get(aktywny+7).widocznosc=false;
                        return false;
                    }
                    else return true;
                }
                if(tmpx==(aktx+2)) {
                    if(pionki.get(aktywny+9).widocznosc==true && pionki.get(aktywny+9).rodzaj==2) {
                        pionki.get(aktywny+9).widocznosc=false;
                        return false;
                    }
                    else return true;
                }
                else return true;
            }
        }

        if(pionki.get(aktywny).rodzaj==2) {
            if(tmpy>=akty || tmpy<=(akty-3)) return true;
            if(tmpy==(akty-1)) {
                if(tmpx==(aktx-1) || tmpx==(aktx+1)) return false;
                else return true;
            }
            if(tmpy==(akty-2)) {
                if(tmpx==(aktx+2)) {
                    if(pionki.get(aktywny-7).widocznosc==true && pionki.get(aktywny-7).rodzaj==1) {
                        pionki.get(aktywny-7).widocznosc=false;
                        return false;
                    }
                    else return true;
                }
                if(tmpx==(aktx-2)) {
                    if(pionki.get(aktywny-9).widocznosc==true && pionki.get(aktywny-9).rodzaj==1) {
                        pionki.get(aktywny-9).widocznosc=false;
                        return false;
                    }
                    else return true;
                }
                else return true;
            }
        }

        return false;
    }

    public void redraw() {
        g.clearRect(0,0,800,800);
        for (Pionek pionek : pionki) {
            if(pionek.widocznosc == false);
            else {
                if(pionek.rodzaj == 1) g.setFill(Color.RED);
                else if (pionek.rodzaj == 2) g.setFill(Color.WHITE);
                if (pionek.akt) g.setFill(Color.BLACK);
                g.fillOval(pionek.x,pionek.y,80,80);
            }
        }
    }

    public void showName(double x,double y) {
        double tmpx = x/100;
        int indx = (int) tmpx;
        double tmpy = y/100;
        int indy = (int) tmpy;
        System.out.println(rows[indy] + " " + columns[indx]);
    }

    @Override
    public void start(Stage primaryStage) {

        Scene scene = new Scene(root, 800, 800, Color.GREEN);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        Group group = new Group();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                rectangles.add(new Rectangle(j*100,i*100,100,100));
                if(table[i][j] == 0) rectangles.get(i*8+j).setFill(Color.IVORY);
                else rectangles.get(i*8+j).setFill(Color.DARKKHAKI);
            }
        }
        for (Rectangle rect : rectangles) {
            group.getChildren().add(rect);
        }
        root.getChildren().add(group);

        root.getChildren().add(canvas);

        for (int i = 0; i < 64; i++) {
            if(i<24) {
                if(table[(i-(i%8))/8][i%8]==0)
                    pionki.add(new Pionek((i%8)*100+10,(i-(i%8))/8*100+10,1,false, false));
                else
                    pionki.add(new Pionek((i%8)*100+10,(i-(i%8))/8*100+10,1,false, true));
            }
            if(i>=24 && i<40)
                pionki.add(new Pionek((i%8)*100+10,(i-(i%8))/8*100+10,1,false, false));
            if(i>=40) {
                if(table[(i-(i%8))/8][i%8]==0)
                    pionki.add(new Pionek((i%8)*100+10,(i-(i%8))/8*100+10,2,false, false));
                else
                    pionki.add(new Pionek((i%8)*100+10,(i-(i%8))/8*100+10,2,false, true));
            }

        }

        redraw();

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                showName(x,y);
                checkAktywny(x,y);
                redraw();
            }
        });

        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }

}
